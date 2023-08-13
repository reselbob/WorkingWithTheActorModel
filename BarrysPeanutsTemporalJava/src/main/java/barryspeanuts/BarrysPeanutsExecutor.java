package barryspeanuts;

import barryspeanuts.mock.MockHelper;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarrysPeanutsExecutor {

  static final String TASK_QUEUE = "BarryPeanutsJava";
  static final String WORKFLOW_ID = TASK_QUEUE + "-" + UUID.randomUUID();
  private static final Logger logger = LoggerFactory.getLogger(BarrysPeanutsExecutor.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) {
    // gRPC stubs wrapper that talks to the local docker instance of temporal
    // service.
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(TASK_QUEUE);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(ShoppingCartWorkflowImpl.class);

    // Start all workers created by this factory.
    factory.start();
    logger.info("Worker started for task queue: {} with WorkflowID : {}", TASK_QUEUE, WORKFLOW_ID);

    // now we can start running instances of our workflow - its state will be persisted
    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .setWorkflowId(WORKFLOW_ID)
            // set the return options
            .setRetryOptions(
                RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(1))
                    .setMaximumInterval(Duration.ofSeconds(10))
                    .build())
            .build();

    ShoppingCartWorkflow wf = client.newWorkflowStub(ShoppingCartWorkflow.class, options);
    try {

      PurchaseItem purchaseItem = MockHelper.getPurchaseItem();
      WorkflowClient.start(wf::startWorkflow);
      // Add some purchase items to the workflow for processing
      wf.addItem( purchaseItem);
      wf.addItem(purchaseItem);
      wf.addItem(purchaseItem);
      wf.addItem(purchaseItem);

      List<PurchaseItem> purchaseItems = wf.queryPurchaseItems();

      Purchase purchase = new Purchase(purchaseItems);


      // Checkout
      wf.checkOut(purchase);
      // TODO Use the Temporal Saga Library
      //
      // (https://www.javadoc.io/static/io.temporal/temporal-sdk/1.0.0/io/temporal/workflow/Saga.html)
      //  to create a compensation if something goes wrong with Checkout

      String firstName = purchase.getPurchaseItems().get(0).getCustomer().getFirstName();
      String lastName = purchase.getPurchaseItems().get(0).getCustomer().getLastName();
      CreditCard creditCard = MockHelper.getCreditCard(firstName, lastName);

      // Pay
      wf.pay(purchase, creditCard);
      // TODO Create a compensation for Pay

      // Ship
      wf.ship(purchase, "FEDEX");
      // TODO Create a compensation for Ship

      purchaseItems = wf.queryPurchaseItems();
      logger.info("The count of purchase items is {}", purchaseItems.toArray().length);

      // Empty out the cart
      wf.completeShoppingCart();
      // TODO Create a compensation for completing the Shopping Cart

      purchaseItems = wf.queryPurchaseItems();

      logger.info(
          "The count of purchase items after the shopping cart is completed is {}",
          purchaseItems.toArray().length);

    } catch (WorkflowException e) {
      // TODO Execute Saga.compensate() here
      throw e;
    }
    logger.info("Nothing left to do, so the Executor will exit. That's all folks!");
  }
}

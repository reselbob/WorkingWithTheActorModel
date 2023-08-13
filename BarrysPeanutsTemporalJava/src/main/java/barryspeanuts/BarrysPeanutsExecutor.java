package barryspeanuts;

import barryspeanuts.mock.MockHelper;
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
      wf.addItem(WORKFLOW_ID, purchaseItem);
      wf.addItem(WORKFLOW_ID, purchaseItem);
      wf.addItem(WORKFLOW_ID, purchaseItem);
      wf.addItem(WORKFLOW_ID, purchaseItem);

      // Checkout
      wf.checkOut(WORKFLOW_ID);
      // TODO Use the Temporal Saga Library
      //
      // (https://www.javadoc.io/static/io.temporal/temporal-sdk/1.0.0/io/temporal/workflow/Saga.html)
      //  to create a compensation if something goes wrong with Checkout

      // Pay
      wf.pay(WORKFLOW_ID);
      // TODO Create a compensation for Pay

      // Ship
      wf.ship(WORKFLOW_ID);
      // TODO Create a compensation for Ship

      List<PurchaseItem> purchaseItems = wf.queryPurchaseItems(WORKFLOW_ID);
      logger.info("The count of purchase items is {}", purchaseItems.toArray().length);

      // Empty out the cart
      wf. completeShoppingCart(WORKFLOW_ID);
      // TODO Create a compensation for exiting the Shopping Cart

      purchaseItems = wf.queryPurchaseItems(WORKFLOW_ID);

      logger.info(
          "The count of purchase items after the shopping cart is completed is {}",
          purchaseItems.toArray().length);

    } catch (WorkflowException e) {
      // TODO Execute Saga.compensate() here
      throw e;
    }

    System.exit(0);
  }
}

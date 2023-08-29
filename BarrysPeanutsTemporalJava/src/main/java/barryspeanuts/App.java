package barryspeanuts;

import barryspeanuts.model.*;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  static final String TASK_QUEUE = "BarryPeanutsTemporal";
  private static final Logger logger = LoggerFactory.getLogger(App.class);

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

    logger.info("Worker listening on task queue: {}.", TASK_QUEUE);

    //Declare the WORKFLOW_ID
    String WORKFLOW_ID = TASK_QUEUE + "-" + UUID.randomUUID();
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
      Address address =
          new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");

      Customer customer =
          new Customer(
              UUID.randomUUID(),
              "Josiah",
              "Bartlet",
              "prez@whitehouse.gove",
              "202 456 1414",
              address);

      WorkflowClient.start(wf::startWorkflow);

      // Create purchase items and add them to the Shopping Cart
      List<PurchaseItem> purchaseItems = new ArrayList<>();

      purchaseItems.add(
          new PurchaseItem(
              UUID.randomUUID(),
              customer,
              "Barry's Deluxe Peanuts:",
              3,
              new BigDecimal("12.99"),
              new BigDecimal("5")));

      purchaseItems.add(
          new PurchaseItem(
              UUID.randomUUID(),
              customer,
              "Barry's Smoked Peanuts",
              5,
              new BigDecimal("25.99"),
              new BigDecimal("2")));

      purchaseItems.add(
          new PurchaseItem(
              UUID.randomUUID(),
              customer,
              "Barry's Soft Shell Peanuts",
              1,
              new BigDecimal("9.99"),
              new BigDecimal("1")));

      wf.addItems(purchaseItems);

      // Get the customer information for the credit card
      String firstName = customer.getFirstName();
      String lastName = customer.getLastName();
      CreditCard creditCard =
          new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);

      // Checkout using the customer's base address as the billing and shipping addresses
      CheckoutInfo checkoutInfo =
          new CheckoutInfo(creditCard, customer.getAddress(), customer.getAddress(), "FEDEX");

      wf.checkOut(checkoutInfo);

      // Exit the workflow
      wf.exit();

    } catch (Exception e) {
      // Just rethrow for now
      throw e;
    }
    logger.info("Nothing left to do, so the Executor will exit. That's all folks!");
  }
}

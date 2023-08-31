package barryspeanuts;

import barryspeanuts.model.*;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.math.BigDecimal;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowRunner {
  static final String TASK_QUEUE = "BarryPeanuts";

  private static final Logger logger = LoggerFactory.getLogger(WorkflowRunner.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) {
    // gRPC stubs wrapper that talks to the local docker instance of temporal
    // service.
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Declare the WORKFLOW_ID
    String WORKFLOW_ID = TASK_QUEUE + "-" + "01";

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
              "Customer01", "Josiah", "Bartlet", "prez@whitehouse.gove", "202 456 1414", address);

      WorkflowClient.start(wf::startWorkflow);

      // Create purchase items and add them to the Shopping Cart via a signal
      wf.addItem(
          new PurchaseItem(
              "PI01",
              customer,
              "Barry's Deluxe Peanuts:",
              3,
              new BigDecimal("12.99"),
              new BigDecimal("5")));

      wf.addItem(
          new PurchaseItem(
              "PI02",
              customer,
              "Barry's Smoked Peanuts",
              5,
              new BigDecimal("25.99"),
              new BigDecimal("2")));

      wf.addItem(
          new PurchaseItem(
              "PI03",
              customer,
              "Barry's Soft Shell Peanuts",
              1,
              new BigDecimal("9.99"),
              new BigDecimal("1")));

      // Get the customer information for the credit card
      String firstName = customer.getFirstName();
      String lastName = customer.getLastName();
      CreditCard creditCard =
          new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);

      // Checkout using the customer's base address as the billing and shipping addresses
      CheckoutInfo checkoutInfo =
          new CheckoutInfo(creditCard, customer.getAddress(), customer.getAddress(), "FEDEX");

      // Send a checkOut signal
      wf.checkOut(checkoutInfo);

      //exit the workflow.
      wf.exit();

    } catch (Exception e) {
      // Just rethrow for now
      throw e;
    }
    logger.info("Nothing left to do, so the Executor will exit. That's all folks!");
  }
}

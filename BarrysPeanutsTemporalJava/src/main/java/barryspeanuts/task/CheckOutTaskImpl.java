package barryspeanuts.task;

import barryspeanuts.ShoppingCartActivities;
import barryspeanuts.ShoppingCartWorkflow;
import barryspeanuts.ShoppingCartWorkflowImpl;
import barryspeanuts.model.Purchase;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class CheckOutTaskImpl implements WorkflowTask {

  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);

  private final Purchase purchase;

  public CheckOutTaskImpl(Purchase purchase) {
    this.purchase = purchase;
  }

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    ShoppingCartActivities activities = shoppingCartWorkflow.queryActivities();
    String fullName =
        String.format(
            "%s %s",
            shoppingCartWorkflow.queryPurchaseItems().get(0).getCustomer().getFirstName(),
            shoppingCartWorkflow.queryPurchaseItems().get(0).getCustomer().getLastName());
    logger.info("{} is checking out customer {}", CheckOutTaskImpl.class, fullName);

    // call the activity
    activities.checkOut(this.purchase.getPurchaseItems());
  }
}

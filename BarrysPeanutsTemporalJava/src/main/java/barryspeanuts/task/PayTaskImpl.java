package barryspeanuts.task;

import barryspeanuts.ShoppingCartActivities;
import barryspeanuts.ShoppingCartWorkflow;
import barryspeanuts.ShoppingCartWorkflowImpl;
import barryspeanuts.mock.mockHelper;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Purchase;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class PayTaskImpl implements WorkflowTask {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private final Purchase purchase;

  public PayTaskImpl(Purchase purchase) {
    this.purchase = purchase;
  }

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    ShoppingCartActivities activities = shoppingCartWorkflow.queryActivities();
    CreditCard creditCard =
        mockHelper.getCreditCard(
            this.purchase.getPurchaseItems().get(0).getCustomer().getFirstName(),
            this.purchase.getPurchaseItems().get(0).getCustomer().getLastName());
    logger.info("{}is Paying on CreditCard for {}", PayTaskImpl.class, creditCard.getFullName());

    activities.pay(this.purchase, creditCard);
  }
}

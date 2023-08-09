package barryspeanuts.task;

import barryspeanuts.ShoppingCartActivities;
import barryspeanuts.ShoppingCartWorkflow;
import barryspeanuts.ShoppingCartWorkflowImpl;
import barryspeanuts.model.Purchase;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class ShipTaskImpl implements WorkflowTask {

  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private final Purchase purchase;
  private final String shipper;

  public ShipTaskImpl(Purchase purchase, String shipper) {
    this.shipper = shipper;
    this.purchase = purchase;
  }

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    ShoppingCartActivities activities = shoppingCartWorkflow.queryActivities();
    logger.info("{} is shipping to {}", ShipTaskImpl.class, this.shipper);
    activities.ship(this.purchase, this.shipper);
  }
}

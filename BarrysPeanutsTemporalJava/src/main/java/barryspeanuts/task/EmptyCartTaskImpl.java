package barryspeanuts.task;

import barryspeanuts.ShoppingCartWorkflow;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class EmptyCartTaskImpl implements WorkflowTask {

  private static final Logger logger = Workflow.getLogger(EmptyCartTaskImpl.class);

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    logger.info("Emptying cart of {}", shoppingCartWorkflow.queryPurchaseItems());
    shoppingCartWorkflow.clearItems();
    logger.info(
        "Emptied cart. The cart now has {} items", shoppingCartWorkflow.queryPurchaseItems());
  }
}

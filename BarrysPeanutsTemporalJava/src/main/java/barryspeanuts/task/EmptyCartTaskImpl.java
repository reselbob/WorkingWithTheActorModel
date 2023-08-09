package barryspeanuts.task;

import barryspeanuts.ShoppingCartWorkflow;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class EmptyCartTaskImpl implements WorkflowTask {

  private static final Logger logger = Workflow.getLogger(EmptyCartTaskImpl.class);

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    logger.info(
        "{} : is emptying the cart of {}",
        EmptyCartTaskImpl.class,
        shoppingCartWorkflow.queryPurchaseItems());
    shoppingCartWorkflow.clearItems();
    logger.info(
        "{} : has emptied the cart which now has {} item",
        EmptyCartTaskImpl.class,
        shoppingCartWorkflow.queryPurchaseItems());
  }
}

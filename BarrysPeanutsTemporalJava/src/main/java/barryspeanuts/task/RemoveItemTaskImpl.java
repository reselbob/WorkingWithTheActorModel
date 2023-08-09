package barryspeanuts.task;

import barryspeanuts.ShoppingCartWorkflow;
import barryspeanuts.model.PurchaseItem;

public class RemoveItemTaskImpl implements WorkflowTask {
  private PurchaseItem purchaseItem;

  public RemoveItemTaskImpl(PurchaseItem purchaseItem) {
    this.purchaseItem = purchaseItem;
  }

  @Override
  public void process(ShoppingCartWorkflow shoppingCartWorkflow) {
    shoppingCartWorkflow.removeItem(this.purchaseItem);
  }
}

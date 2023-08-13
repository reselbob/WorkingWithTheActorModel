package barryspeanuts;

import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);

  boolean exit = false;
  List<PurchaseItem> purchaseItems = new ArrayList<>();
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);

  @Override
  public void startWorkflow() {

    logger.info("Starting Workflow for Barry's Peanuts");
    while (true) {
      Workflow.await(() -> exit);
    }
  }

  @Override
  public List<PurchaseItem> queryPurchaseItems() {
    return this.purchaseItems;
  }

  @Override
  public void addItem(PurchaseItem purchaseItem) {
    this.purchaseItems.add(purchaseItem);
  }

  @Override
  public void removeItem(PurchaseItem purchaseItem) {
    this.purchaseItems.remove(purchaseItem);
  }

  @Override
  public void checkOut(Purchase purchase) {

    logger.info("Checking out purchase id {}", purchase.getId());
  }

  @Override
  public void pay(Purchase purchase, CreditCard creditCard) {
    logger.info("Paying for purchase] id {} using credit card number {}",
            purchase.getId(),
            creditCard.getNumber());
  }

  @Override
  public void ship(Purchase purchase, String shipper) {
    logger.info("Shipping purchase id {} using {} ", purchase.getId(), shipper);
  }

  @Override
  public void completeShoppingCart() {
    logger.info("Shopping Cart process");
    this.purchaseItems = new ArrayList<>();
  }

  /**
   * This is convenience signal to shut down the workflow
   */
  @Override
  public void exit(){
    exit = true;
  }
}

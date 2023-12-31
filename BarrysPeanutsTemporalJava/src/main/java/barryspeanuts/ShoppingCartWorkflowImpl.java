package barryspeanuts;

import barryspeanuts.model.CheckoutInfo;
import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private final List<PurchaseItem> purchaseItems = new ArrayList<>();
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);
  private boolean exit = false;

  @Override
  public void startWorkflow() {
    logger.info("Starting Workflow for Barry's Peanuts");
    Workflow.await(() -> exit);
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
  public void checkOut(CheckoutInfo checkoutInfo) {
    logger.info(
        "Paying for purchase using credit card number {} for {} purchase item at billing address {}.",
        checkoutInfo.getCreditCard(),
        this.purchaseItems.toArray().length,
        checkoutInfo.getBillingAddress());

    logger.info(
        "Shipping {} purchase items using {} to shipping address {}.",
        this.purchaseItems.toArray().length,
        checkoutInfo.getShipper(),
        checkoutInfo.getShippingAddress());

    // remove items from
    logger.info(
        "Removing {} purchase items from the Shopping Cart. The Shopping Cart is now clear.",
        this.purchaseItems.toArray().length);
    this.purchaseItems.clear();
  }

  /*
  This is a convenience method to clear the Shopping cart
   */
  public void removeAllItems() {
    logger.info("Removing {} purchase items.", this.purchaseItems.toArray().length);

    this.purchaseItems.clear();

    logger.info(
        "Removed purchase items. There are now {} purchase items in the shopping cart.",
        this.purchaseItems.toArray().length);
  }

  /** This is convenience signal to shut down the workflow */
  @Override
  public void exit() {
    logger.info("Exiting the shopping cart");
    exit = true;
  }
}

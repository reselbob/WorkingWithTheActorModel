package barryspeanuts;

import barryspeanuts.model.Address;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private boolean exit = false;

  private final List<PurchaseItem> purchaseItems = new ArrayList<>();
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);

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
  public void addItems(List<PurchaseItem> purchaseItems) {
    this.purchaseItems.addAll(purchaseItems);
  }

  @Override
  public void removeItem(PurchaseItem purchaseItem) {
    this.purchaseItems.remove(purchaseItem);
  }

  @Override
  public void checkOut() {
    logger.info("Checking out {} purchase items.", this.purchaseItems.toArray().length);
  }

  @Override
  public void pay(Address billingAddress, CreditCard creditCard) {
    for (PurchaseItem purchaseItem : this.purchaseItems) {
      purchaseItem.setBillingAddress(Optional.ofNullable(billingAddress));
    }

    JSONObject jsonObj = new JSONObject(billingAddress);
    logger.info(
        "Paying for purchase using credit card number {} for {} purchase item at billing address {}.",
        creditCard.getNumber(),
        this.purchaseItems.toArray().length,
        billingAddress.toString());
  }

  @Override
  public void ship(Address shippingAddress, String shipper) {
    for (PurchaseItem purchaseItem : this.purchaseItems) {
      purchaseItem.setShippingAddress(Optional.ofNullable(shippingAddress));
    }

    JSONObject jsonObj = new JSONObject(shippingAddress);
    logger.info(
        "Shipping {} purchase items using {} to shipping address {}.",
        this.purchaseItems.toArray().length,
        shipper,
        jsonObj);
  }

  @Override
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

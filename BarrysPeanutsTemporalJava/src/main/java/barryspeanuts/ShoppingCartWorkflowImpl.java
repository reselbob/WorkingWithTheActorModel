package barryspeanuts;

import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);

  boolean exit = false;
  List<PurchaseItem> purchaseItems = new ArrayList<>();

  Purchase purchase = new Purchase(UUID.randomUUID(), purchaseItems);
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
  public void addItem(PurchaseItem purchaseItem) {
    this.purchaseItems.add(purchaseItem);
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
    logger.info("Checking out {} purchase items for purchase id {}.",
            purchase.getPurchaseItems().toArray().length,
            purchase.getId());
  }

  @Override
  public void pay(CreditCard creditCard) {
    // Use the Customer's base address as the billing address
    for (PurchaseItem purchaseItem : this.purchaseItems) {
      purchaseItem.setBillingAddress(Optional.ofNullable(purchaseItem.getCustomer().getAddress()));
    }

    logger.info(
        "Paying for purchase id {} using credit card number {} for {} purchase item.",
        purchase.getId(),
        creditCard.getNumber(),
        purchase.getPurchaseItems().toArray().length);
  }

  @Override
  public void ship(String shipper) {
    // Use the Customer's base address as the Shipping address
    for (PurchaseItem purchaseItem : this.purchaseItems) {
      purchaseItem.setShippingAddress(Optional.ofNullable(purchaseItem.getCustomer().getAddress()));
    }
    logger.info(
        "Shipping {} purchase items for purchase id {} using {}.",
        purchase.getPurchaseItems().toArray().length,
        purchase.getId(),
        shipper);
  }

  @Override
  public void removeAllItems() {

    logger.info("Removing {} purchase items for purchase id {}.",
            purchase.getPurchaseItems().toArray().length,
            purchase.getId()
            );

    this.purchaseItems.clear();

    logger.info("Removed purchase items for purchase id {}. There are now {} purchase items in the shopping cart.",
            purchase.getId(),
            purchase.getPurchaseItems().toArray().length
            );
  }

  /** This is convenience signal to shut down the workflow */
  @Override
  public void exit() {
    logger.info("Exiting the shopping cart");
    exit = true;
  }
}

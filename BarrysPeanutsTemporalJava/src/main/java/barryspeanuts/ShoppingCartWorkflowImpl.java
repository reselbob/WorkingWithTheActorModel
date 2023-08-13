package barryspeanuts;

import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.util.ArrayList;
import java.util.Date;
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
      //Workflow.await(() -> !messageQueue.isEmpty() || exit);
    }
  }

  @Override
  public List<PurchaseItem> queryPurchaseItems(String workflowId) {
    if(workflowId == null) throw new NullPointerException();
    return this.purchaseItems;
  }


  @Override
  public void addItem(String workflowId, PurchaseItem purchaseItem) {
    if(workflowId == null) throw new NullPointerException();
    this.purchaseItems.add(purchaseItem);
  }

  @Override
  public void removeItem(String workflowId,PurchaseItem purchaseItem) {
    if(workflowId == null) throw new NullPointerException();
    this.purchaseItems.remove(purchaseItem);
  }

  @Override
  public void checkOut(String workflowId) {
    if(workflowId == null) throw new NullPointerException();
    logger.info("Checking Out according to WorkflowId: {}", workflowId);
  }

  @Override
  public void pay(String workflowId) {
    if(workflowId == null) throw new NullPointerException();
    logger.info("Paying according to WorkflowId: {}", workflowId);
  }

  @Override
  public void ship(String workflowId) {
    if(workflowId == null) throw new NullPointerException();
    logger.info("Shipping according to WorkflowId: {}", workflowId);
  }

  @Override
  public void  completeShoppingCart(String workflowId) {
    if(workflowId == null) throw new NullPointerException();
    logger.info("Shopping Cart process is completing according to WorkflowId: {}", workflowId);
    this.purchaseItems = new ArrayList<>();
  }

  Purchase getPurchase(String workflowId) {
    if(workflowId == null) throw new NullPointerException();

    List<PurchaseItem> items = queryPurchaseItems(workflowId);
    return new Purchase(items, new Date());
  }
}

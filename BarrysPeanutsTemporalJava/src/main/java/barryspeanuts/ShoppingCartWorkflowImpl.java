package barryspeanuts;

import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import barryspeanuts.task.CheckOutTaskImpl;
import barryspeanuts.task.PayTaskImpl;
import barryspeanuts.task.ShipTaskImpl;
import barryspeanuts.task.WorkflowTask;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  ShoppingCartActivities activities = new ShoppingCartActivitiesImpl();
  List<PurchaseItem> purchaseItems;
  private WorkflowQueue<WorkflowTask> queue = Workflow.newWorkflowQueue(1024);

  @Override
  public void startWorkflow() {
    logger.info("Starting Workflow for Barry's Peanuts");
    while (true) {
      if (Workflow.getInfo().getHistoryLength() > 2000) {
        // do continue as new here
        logger.info("Workflow history greater than 2000");
        // System.out.println("Workflow history greater than 2000");
      }

      WorkflowTask task = this.queue.cancellablePoll(Duration.ofDays(30));
      if (task == null) {
        logger.info("Breaking task");
        break;
      }
      task.process(this);
    }
  }

  @Override
  public ShoppingCartActivities queryActivities() {
    return this.activities;
  }

  @Override
  public List<PurchaseItem> queryPurchaseItems() {
    return this.purchaseItems;
  }

  @Override
  public void addItem(PurchaseItem purchaseItem) {
    if (this.purchaseItems == null) {
      this.purchaseItems = new ArrayList<>();
    }
    this.purchaseItems.add(purchaseItem);
  }

  @Override
  public void removeItem(PurchaseItem purchaseItem) {
    if (this.purchaseItems != null) {
      this.purchaseItems.remove(purchaseItem);
    }
  }

  public void clearItems() {
    this.purchaseItems = new ArrayList<>();
  }

  @Override
  public void checkOut(String message) {
    this.queue.put(new CheckOutTaskImpl(this.getPurchase()));
  }

  @Override
  public void pay(String message) {
    this.queue.put(new PayTaskImpl(this.getPurchase()));
  }

  @Override
  public void ship(String message) {
    this.queue.put(new ShipTaskImpl(this.getPurchase(), "FEDEX"));
  }

  @Override
  public void emptyCart(String message) {
    this.purchaseItems = new ArrayList<>();
  }

  Purchase getPurchase() {
    List<PurchaseItem> items = queryPurchaseItems();
    return new Purchase(items, new Date());
  }
}

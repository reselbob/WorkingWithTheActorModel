package barryspeanuts;

import barryspeanuts.model.PurchaseItem;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.List;

@WorkflowInterface
public interface ShoppingCartWorkflow {
  @WorkflowMethod
  void startWorkflow();

  @QueryMethod
  ShoppingCartActivities queryActivities();

  @QueryMethod
  List<PurchaseItem> queryPurchaseItems();

  @SignalMethod
  void addItem(PurchaseItem purchaseItem);

  @SignalMethod
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void clearItems();

  @SignalMethod
  void checkOut(String message);

  @SignalMethod
  void pay(String message);

  @SignalMethod
  void ship(String message);

  @SignalMethod
  void emptyCart(String message);
}

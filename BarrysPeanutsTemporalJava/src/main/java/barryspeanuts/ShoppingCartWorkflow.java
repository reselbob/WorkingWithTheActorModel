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
  List<String> startWorkflow();

  @QueryMethod
  List<PurchaseItem> queryPurchaseItems(String workflowId);

  @SignalMethod
  void addItem(String workflowId, PurchaseItem purchaseItem);

  @SignalMethod
  void removeItem(String workflowId, PurchaseItem purchaseItem);

  @SignalMethod
  void checkOut(String workflowId);

  @SignalMethod
  void pay(String workflowId);

  @SignalMethod
  void ship(String workflowId);

  @SignalMethod
  void  completeShoppingCart(String workflowId);
}

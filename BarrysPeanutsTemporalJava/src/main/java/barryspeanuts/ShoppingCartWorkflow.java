package barryspeanuts;

import barryspeanuts.model.CheckoutInfo;
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
  List<PurchaseItem> queryPurchaseItems();

  @SignalMethod
  void addItems(List<PurchaseItem> purchaseItems);

  @SignalMethod
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void checkOut(CheckoutInfo checkoutInfo);

  @SignalMethod
  void removeAllItems();

  @SignalMethod
  void exit();
}

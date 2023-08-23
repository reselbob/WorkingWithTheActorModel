package barryspeanuts;

import barryspeanuts.model.CreditCard;
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
  void addItem(PurchaseItem purchaseItem);

  @SignalMethod
  void addItems(List<PurchaseItem> purchaseItems);

  @SignalMethod
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void checkOut();

  @SignalMethod
  void pay(CreditCard creditcard);

  @SignalMethod
  void ship(String shipper);

  @SignalMethod
  void removeAllItems();

  @SignalMethod
  void exit();
}

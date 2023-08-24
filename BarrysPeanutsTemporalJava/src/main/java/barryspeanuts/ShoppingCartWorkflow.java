package barryspeanuts;

import barryspeanuts.model.Address;
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
  void addItems(List<PurchaseItem> purchaseItems);

  @SignalMethod
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void checkOut();

  @SignalMethod
  void pay(Address billingAddress, CreditCard creditcard);

  @SignalMethod
  void ship(Address shippingAddress, String shipper);

  @SignalMethod
  void removeAllItems();

  @SignalMethod
  void exit();
}

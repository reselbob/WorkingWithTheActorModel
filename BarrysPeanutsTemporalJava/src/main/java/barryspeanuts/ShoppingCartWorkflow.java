package barryspeanuts;

import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Purchase;
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
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void checkOut(Purchase purchase);

  @SignalMethod
  void pay(Purchase purchase, CreditCard creditcard);

  @SignalMethod
  void ship(Purchase purchase, String shipper);

  @SignalMethod
  void resetShoppingCart(List<PurchaseItem> purchaseItems);

  @SignalMethod
  void exit();
}

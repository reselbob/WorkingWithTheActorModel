package barryspeanuts;

import barryspeanuts.model.*;
import io.temporal.activity.ActivityInterface;
import java.util.List;

@ActivityInterface
public interface ShoppingCartActivities {
  CheckOutReceipt checkOut(List<PurchaseItem> purchaseItems);

  PaymentReceipt pay(Purchase purchase, CreditCard creditCard);

  ShippingReceipt ship(Purchase purchase, String shipper);
}

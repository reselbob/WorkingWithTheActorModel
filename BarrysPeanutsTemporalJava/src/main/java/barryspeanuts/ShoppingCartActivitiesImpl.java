package barryspeanuts;

import barryspeanuts.model.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCartActivitiesImpl implements ShoppingCartActivities {

  private static final Logger logger = LoggerFactory.getLogger(ShoppingCartActivitiesImpl.class);

  @Override
  public CheckOutReceipt checkOut(List<PurchaseItem> purchaseItems) {
    logger.info(
        "{} {} is checking out",
        purchaseItems.get(0).getCustomer().getFirstName(),
        purchaseItems.get(0).getCustomer().getLastName());
    Purchase purchase = new Purchase(purchaseItems, new Date());
    return new CheckOutReceipt(purchase, new Date());
  }

  public PaymentReceipt pay(Purchase purchase, CreditCard creditCard) {
    logger.info("{} is paying", creditCard.getFullName());
    UUID transactionId = UUID.randomUUID();
    return new PaymentReceipt(purchase, new Date(), creditCard, transactionId);
  }

  public ShippingReceipt ship(Purchase purchase, String shipper) {
    logger.info(
        "{} is shipping to {} {}",
        shipper,
        purchase.getPurchaseItems().get(0).getCustomer().getFirstName(),
        purchase.getPurchaseItems().get(0).getCustomer().getLastName());
    return new ShippingReceipt(purchase, shipper);
  }
}

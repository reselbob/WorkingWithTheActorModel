package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerActor extends AbstractBehavior<Object> {
  Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

  private CustomerActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(CustomerActor::new);
  }

  public static Behavior<Object> behavior() {
    return Behaviors.setup(CustomerActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder()
        .onMessage(PaymentActor.PaymentReceipt.class, this::handlePaymentReceipt)
        .onMessage(ShipperActor.ShippingReceipt.class, this::handleShippingReceipt)
        .build();
  }

  private Behavior<Object> handlePaymentReceipt(PaymentActor.PaymentReceipt msg) {
    String fullName =
        String.format("%s %s", msg.getCustomer().getFirstName(), msg.getCustomer().getLastName());
    logger.info(
        "Customer {} processed PaymentInfo Receipt with ID: {} on {} with CC Number: {} for the amount of: {}",
        fullName,
        msg.getId(),
        msg.getPaymentDate(),
        msg.getCreditCardNumber(),
        msg.getAmount());
    return this;
  }

  private Behavior<Object> handleShippingReceipt(ShipperActor.ShippingReceipt msg) {
    Date today = new Date();
    ArrayList<PurchaseItem> items = msg.getPurchaseItems();
    String firstName = null;
    String lastName = null;
    String shipper = msg.getShipper();
    Date shipDate = new Date();
    for (PurchaseItem item : items) {
      firstName = item.getCustomer().getFirstName();
      lastName = item.getCustomer().getLastName();
      item.setShipDate(shipDate);
    }
    logger.info(
        "Shipped {} purchases to {} {} using {}. \n",
        items.toArray().length,
        firstName,
        lastName,
        shipper);
    return this;
  }

  public static class CreditCardRequest {
    ArrayList<PurchaseItem> purchaseItems;

    public CreditCardRequest(ArrayList<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }
  }
}

package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerActor extends AbstractBehavior<Object> {
  private static final Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

  private CustomerActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
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
    logger.info(
        "Customer {} {} processed PaymentInfo Receipt with ID: {} on {} with CC Number: {} for the amount of: {}",
        msg.getCustomer().getFirstName(),
        msg.getCustomer().getLastName(),
        msg.getId(),
        msg.getPaymentDate(),
        msg.getCreditCardNumber(),
        msg.getAmount());
    return this;
  }

  private Behavior<Object> handleShippingReceipt(ShipperActor.ShippingReceipt msg) {
    Date today = new Date();
    List<PurchaseItem> items = msg.getPurchaseItems();
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
    private final List<PurchaseItem> purchaseItems;

    public CreditCardRequest(ArrayList<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }
  }
}

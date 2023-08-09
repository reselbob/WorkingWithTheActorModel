package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.helper.MockHelper;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentActor extends AbstractBehavior<Object> {
  Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

  private PaymentActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(PaymentActor::new);
  }

  public static Behavior<Object> behavior() {
    return Behaviors.setup(PaymentActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder().onMessage(PaymentInfo.class, this::handlePayment).build();
  }

  private Behavior<Object> handlePayment(PaymentInfo msg) {
    CreditCard creditCard = msg.getCreditCard();

    double amount = 0;
    for (PurchaseItem item : msg.getPurchaseItems()) {
      amount = amount + item.getTotal();
    }
    // Now pay
    logger.info(
        "Paying with Credit Card for {} with Credit Card Number {} on {} for the amount of {}\n",
        creditCard.getNameOnCard(),
        creditCard.getCreditCardNumber(),
        new Date(),
        amount);
    // send a receipt to the Customer
    PaymentActor.PaymentReceipt paymentReceipt =
        new PaymentActor.PaymentReceipt(
            msg.customer, new Date(), msg.creditCard.getCreditCardNumber(), amount);
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(paymentReceipt);
    // and ship the purchase
    ActorSystem<Object> shipperActor = ActorSystem.create(ShipperActor.create(), "shipperActor");
    ShipperActor.ShipmentInfo shipmentInfo =
        new ShipperActor.ShipmentInfo(MockHelper.getShipper(), msg.getPurchaseItems());
    shipperActor.tell(shipmentInfo);
    return this;
  }

  public static class PaymentInfo {

    UUID id;
    Customer customer;
    ArrayList<PurchaseItem> purchaseItems;
    CreditCard creditCard;
    double paymentAmount;

    public PaymentInfo(
        Customer customer, CreditCard creditCard, ArrayList<PurchaseItem> purchaseItems) {
      this.id = UUID.randomUUID();
      this.customer = customer;
      this.creditCard = creditCard;
      this.purchaseItems = purchaseItems;
    }

    public UUID getId() {
      return id;
    }

    public Customer getCustomer() {
      return customer;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }

    public CreditCard getCreditCard() {
      return creditCard;
    }

    public double getPaymentAmount() {
      return paymentAmount;
    }
  }

  public static class PaymentReceipt {
    UUID id;
    Customer customer;
    Date paymentDate;
    String creditCardNumber;
    double amount;

    PaymentReceipt(Customer customer, Date paymentDate, String creditCardNumber, double amount) {
      this.id = UUID.randomUUID();
      this.customer = customer;
      this.paymentDate = paymentDate;
      this.creditCardNumber = creditCardNumber;
      this.amount = amount;
    }

    public UUID getId() {
      return id;
    }

    public Customer getCustomer() {
      return customer;
    }

    public Date getPaymentDate() {
      return paymentDate;
    }

    public String getCreditCardNumber() {
      return creditCardNumber;
    }

    public double getAmount() {
      return amount;
    }
  }
}

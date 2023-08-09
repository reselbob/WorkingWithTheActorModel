package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentActor extends AbstractBehavior<Object> {

  private static final Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

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

    double amount = msg.getPaymentAmount();
    // Now pay
    logger.info(
        "{} is Paying with Credit Card for {} with Credit Card Number {} on {} for the amount of {}\n",
        PaymentActor.class,
        creditCard.getNameOnCard(),
        creditCard.getCreditCardNumber(),
        new Date(),
        amount);
    return this;
  }

  public static class PaymentInfo {

    UUID id;
    Customer customer;
    Vector<PurchaseItem> purchaseItems;
    CreditCard creditCard;
    double paymentAmount;

    public PaymentInfo(Customer customer, CreditCard creditCard, double paymentAmount) {
      this.id = UUID.randomUUID();
      this.customer = customer;
      this.creditCard = creditCard;
      this.paymentAmount = paymentAmount;
    }

    public UUID getId() {
      return id;
    }

    public Customer getCustomer() {
      return customer;
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

    public double getPaymentAmount() {
      return amount;
    }
  }
}

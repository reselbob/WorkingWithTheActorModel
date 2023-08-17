package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;
import java.util.Date;
import java.util.UUID;

public class PaymentActor extends AbstractBehavior<Object> {
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

    // Get purchase state
    getContext()
        .getLog()
        .info(
            "TODO: Getting purchase state for payment according to PaymentId {}\n", msg.purchaseId);
    double amount = msg.getPaymentAmount();
    // Now pay
    getContext()
        .getLog()
        .info(
            "Paying with Credit Card for {} with Credit Card Number {} for the amount of {}\n",
            creditCard.getNameOnCard(),
            creditCard.getCreditCardNumber(),
            amount);

    // Save purchase state
    getContext()
        .getLog()
        .info(
            "TODO: Saving purchase state for payment according to PaymentId {}\n", msg.purchaseId);

    // Send a payment receipt
    CustomerActor.PaymentReceipt paymentReceipt = new CustomerActor.PaymentReceipt(msg.purchaseId);
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(paymentReceipt);
    return this;
  }

  public static class PaymentInfo {

    private final UUID id;
    private final Customer customer;
    private final CreditCard creditCard;
    private double paymentAmount;
    private UUID purchaseId;

    public PaymentInfo(Customer customer, CreditCard creditCard) {
      this.id = UUID.randomUUID();
      this.customer = customer;
      this.creditCard = creditCard;
    }

    public PaymentInfo(
        Customer customer, CreditCard creditCard, double paymentAmount, UUID purchaseId) {
      this.id = UUID.randomUUID();
      this.customer = customer;
      this.creditCard = creditCard;
      this.paymentAmount = paymentAmount;
      this.purchaseId = purchaseId;
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

    public void setPaymentAmount(double paymentAmount) {
      this.paymentAmount = paymentAmount;
    }
  }

  public static class PaymentReceipt {
    private final UUID id;
    private final Customer customer;
    private final Date paymentDate;
    private final String creditCardNumber;
    private final double amount;

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

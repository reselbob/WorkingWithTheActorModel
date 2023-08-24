package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;
import java.math.BigDecimal;
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
            "TODO: Getting purchase state for payment according for customer {} {} or "
                + "relying upon the Akka Event Sourcing framework.",
            msg.getCustomer().getFirstName(),
            msg.getCustomer().getLastName());
    BigDecimal amount = msg.getPaymentAmount();
    // Now pay
    getContext()
        .getLog()
        .info(
            "Paying with Credit Card for {} with Credit Card Number {} for the amount of {}.",
            creditCard.getNameOnCard(),
            creditCard.getCreditCardNumber(),
            amount);

    // Save purchase state
    getContext()
        .getLog()
        .info(
            "TODO: Saving purchase state for payment according for customer {} {} or "
                + "relying upon the Akka Event Sourcing framework.",
            msg.getCustomer().getFirstName(),
            msg.getCustomer().getLastName());

    // Send a payment receipt
    CustomerActor.PaymentReceipt paymentReceipt =
        new CustomerActor.PaymentReceipt(
            UUID.randomUUID(), msg.getCustomer(), msg.getPaymentAmount());
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(paymentReceipt);
    return this;
  }

  public static class PaymentInfo {
    private final UUID id;
    private final Customer customer;
    private final CreditCard creditCard;
    private final BigDecimal paymentAmount;

    public PaymentInfo(
        UUID id, Customer customer, CreditCard creditCard, BigDecimal paymentAmount) {
      this.id = id;
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

    public BigDecimal getPaymentAmount() {
      return paymentAmount;
    }
  }

  public static class PaymentReceipt {
    private final UUID id;
    private final Customer customer;
    private final Date paymentDate;
    private final String creditCardNumber;
    private final BigDecimal amount;

    PaymentReceipt(
        UUID id, Customer customer, Date paymentDate, String creditCardNumber, BigDecimal amount) {
      this.id = id;
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

    public BigDecimal getPaymentAmount() {
      return amount;
    }
  }
}

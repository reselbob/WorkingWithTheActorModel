package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.Customer;
import barryspeanuts.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CustomerActor extends AbstractBehavior<Object> {

  private CustomerActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(CustomerActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder()
        .onMessage(CustomerActor.ShippingReceipt.class, this::handleShippingReceipt)
        .onMessage(CustomerActor.PaymentReceipt.class, this::handlePaymentReceipt)
        .build();
  }

  private Behavior<Object> handleShippingReceipt(CustomerActor.ShippingReceipt msg) {
    getContext()
        .getLog()
        .info(
            "Received shipping confirmation by id {} for {} purchase items.",
            msg.getId(),
            msg.getPurchaseItems().size());
    return this;
  }

  private Behavior<Object> handlePaymentReceipt(CustomerActor.PaymentReceipt msg) {
    getContext()
        .getLog()
        .info(
            "Received payment confirmation by id {} for customer {} {}.",
            msg.getId(),
            msg.getCustomer().getFirstName(),
            msg.getCustomer().getLastName());
    return this;
  }

  public static class ShippingReceipt {
    private final UUID id;
    private final List<PurchaseItem> purchaseItems;

    public ShippingReceipt(UUID id, List<PurchaseItem> purchaseItems) {
      this.id = id;
      this.purchaseItems = purchaseItems;
    }

    public UUID getId() {
      return id;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }
  }

  public static class PaymentReceipt {
    private final UUID id;
    private final Customer customer;
    private final BigDecimal purchaseTotal;

    public PaymentReceipt(UUID id, Customer customer, BigDecimal purchaseTotal) {
      this.id = id;
      this.customer = customer;
      this.purchaseTotal = purchaseTotal;
    }

    public UUID getId() {
      return id;
    }

    public Customer getCustomer() {
      return customer;
    }

    public BigDecimal getPurchaseTotal() {
      return purchaseTotal;
    }
  }
}

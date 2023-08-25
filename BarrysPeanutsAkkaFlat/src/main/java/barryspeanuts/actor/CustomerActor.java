package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.Customer;
import barryspeanuts.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Vector;

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
            "Customer {} {} received shipping confirmation id {} for {} purchase items shipped via {}.",
            msg.getCustomer().getFirstName(),
            msg.getCustomer().getLastName(),
            msg.getId(),
            msg.getPurchaseItems().size(),
            msg.getShipper());
    return this;
  }

  private Behavior<Object> handlePaymentReceipt(CustomerActor.PaymentReceipt msg) {
    getContext()
        .getLog()
        .info(
            "Customer {} {} received payment confirmation id {} for the purchase total of {}.",
            msg.getCustomer().getFirstName(),
            msg.getCustomer().getLastName(),
            msg.getId(),
            msg.getPurchaseTotal());
    return this;
  }

  public static class ShippingReceipt {
    private final UUID id;
    private final Customer customer;
    private final Vector<PurchaseItem> purchaseItems;
    private final String shipper;

    public ShippingReceipt(
        UUID id, Customer customer, Vector<PurchaseItem> purchaseItems, String shipper) {
      this.id = id;
      this.customer = customer;
      this.purchaseItems = purchaseItems;
      this.shipper = shipper;
    }

    public UUID getId() {
      return this.id;
    }

    public Customer getCustomer() {
      return this.customer;
    }

    public Vector<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }

    public String getShipper() {
      return this.shipper;
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
      return this.id;
    }

    public Customer getCustomer() {
      return this.customer;
    }

    public BigDecimal getPurchaseTotal() {
      return this.purchaseTotal;
    }
  }
}

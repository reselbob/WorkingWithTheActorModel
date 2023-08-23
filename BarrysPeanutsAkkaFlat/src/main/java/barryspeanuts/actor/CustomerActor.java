package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
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
            "Received shipping confirmation by id {} for purchaseId {}.",
            msg.getId(),
            msg.getPurchaseId());
    return this;
  }

  private Behavior<Object> handlePaymentReceipt(CustomerActor.PaymentReceipt msg) {
    getContext()
        .getLog()
        .info(
            "Received payment confirmation by id {} for purchaseId {}.",
            msg.getId(),
            msg.getPurchaseId());
    return this;
  }

  public static class ShippingReceipt {
    private final UUID id;
    private final UUID purchaseId;

    public ShippingReceipt(UUID id, UUID purchaseId) {
      this.id = id;
      this.purchaseId = purchaseId;
    }

    public UUID getId() {
      return id;
    }

    public UUID getPurchaseId() {
      return purchaseId;
    }
  }

  public static class PaymentReceipt {
    private final UUID id;
    private final UUID purchaseId;

    public PaymentReceipt(UUID id, UUID purchaseId) {
      this.id = id;
      this.purchaseId = purchaseId;
    }

    public UUID getId() {
      return id;
    }

    public UUID getPurchaseId() {
      return purchaseId;
    }
  }
}

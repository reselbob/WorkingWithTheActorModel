package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.Purchase;
import java.util.Date;
import java.util.UUID;

public class ShipperActor extends AbstractBehavior<Object> {
  private ShipperActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(ShipperActor::new);
  }

  public static Behavior<Object> behavior() {
    return Behaviors.setup(ShipperActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder().onMessage(ShipmentInfo.class, this::handleShipment).build();
  }

  private Behavior<Object> handleShipment(ShipmentInfo msg) {

    String firstName = msg.getPurchase().getPurchaseItems().get(0).getCustomer().getFirstName();
    String lastName = msg.getPurchase().getPurchaseItems().get(0).getCustomer().getLastName();
    Date shipDate = new Date();

    // Get Purchase state
    getContext()
        .getLog()
        .info(
            "TODO: Getting purchase state before shipping for PurchaseId {}. "
                + "or rely upon the Akka Event Sourcing framework",
            msg.getPurchase().getId());
    // Now ship
    getContext()
        .getLog()
        .info(
            "{} {} is Shipping the purchase using Shipper: {} on {}.",
            firstName,
            lastName,
            msg.getShipper(),
            shipDate);
    // Save ship state to data store
    getContext()
        .getLog()
        .info(
            "TODO: Saving the purchase state after shipping for PurchaseId {} or "
                + "relying upon the Akka Event Sourcing framework.",
            msg.getPurchase().getId());

    // Send a receipt
    CustomerActor.ShippingReceipt shippingReceipt =
        new CustomerActor.ShippingReceipt(UUID.randomUUID(), msg.getPurchase().getId());
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(shippingReceipt);
    return this;
  }

  public static class ShipmentInfo {

    private final UUID id;
    private final String shipper;
    private Purchase purchase;

    public ShipmentInfo(UUID id, String shipper) {
      this.id = id;
      this.shipper = shipper;
    }

    public UUID getId() {
      return id;
    }

    public String getShipper() {
      return shipper;
    }

    public Purchase getPurchase() {
      return this.purchase;
    }

    public void setPurchase(Purchase purchase) {
      this.purchase = purchase;
    }
  }

  public static class ShippingReceipt {

    String shipper;
    Purchase purchase;
    Date shipDate;

    public ShippingReceipt(String shipper, Purchase purchase, Date shipDate) {
      this.shipper = shipper;
      this.purchase = purchase;
      this.shipDate = shipDate;
    }

    public String getShipper() {
      return shipper;
    }

    Purchase getPurchas() {
      return this.purchase;
    }

    public Date getShipDate() {
      return shipDate;
    }
  }
}

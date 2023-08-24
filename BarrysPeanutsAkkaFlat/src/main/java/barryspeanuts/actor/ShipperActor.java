package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.Purchase;
import barryspeanuts.model.PurchaseItem;
import java.util.Date;
import java.util.List;
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

    String firstName = msg.getPurchaseItems().get(0).getCustomer().getFirstName();
    String lastName = msg.getPurchaseItems().get(0).getCustomer().getLastName();
    Date shipDate = new Date();

    // Get Purchase state
    getContext()
        .getLog()
        .info(
            "TODO: Getting purchase items state before shipping for {} purchase items. "
                + "or rely upon the Akka Event Sourcing framework.",
            msg.getPurchaseItems().size());
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
            "TODO: Saving the purchase state after shipping for {} purchase items"
                + "relying upon the Akka Event Sourcing framework.",
            msg.getPurchaseItems().size());

    // Send a receipt
    CustomerActor.ShippingReceipt shippingReceipt =
        new CustomerActor.ShippingReceipt(UUID.randomUUID(), msg.getPurchaseItems());
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(shippingReceipt);
    return this;
  }

  public static class ShipmentInfo {
    private final UUID id;
    private final String shipper;
    private final List<PurchaseItem> purchaseItems;

    public ShipmentInfo(UUID id, List<PurchaseItem> purchaseItems, String shipper) {
      this.id = id;
      this.shipper = shipper;
      this.purchaseItems = purchaseItems;
    }

    public UUID getId() {
      return id;
    }

    public String getShipper() {
      return shipper;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }
  }

  public static class ShippingReceipt {
    private final String shipper;
    private final Purchase purchase;
    private final Date shipDate;

    public ShippingReceipt(String shipper, Purchase purchase, Date shipDate) {
      this.shipper = shipper;
      this.purchase = purchase;
      this.shipDate = shipDate;
    }

    public String getShipper() {
      return shipper;
    }

    public Purchase getPurchase() {
      return this.purchase;
    }

    public Date getShipDate() {
      return shipDate;
    }
  }
}

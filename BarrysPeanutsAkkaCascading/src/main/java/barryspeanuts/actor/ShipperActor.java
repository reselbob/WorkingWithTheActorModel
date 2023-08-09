package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipperActor extends AbstractBehavior<Object> {
  Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

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
    // Now ship

    Date shipDate = new Date();
    logger.info("Shipping the purchase using Shipper: {} on {}.\n", msg.getShipper(), shipDate);
    // Send a shipping receipt back toa the Customer
    ShipperActor.ShippingReceipt shippingReceipt =
        new ShipperActor.ShippingReceipt(msg.getShipper(), msg.getPurchaseItems(), shipDate);
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(shippingReceipt);
    return this;
  }

  public static class ShipmentInfo {

    UUID id;
    String shipper;
    ArrayList<PurchaseItem> purchaseItems;

    public ShipmentInfo(String shipper, ArrayList<PurchaseItem> purchaseItems) {
      this.id = UUID.randomUUID();
      this.shipper = shipper;
      this.purchaseItems = purchaseItems;
    }

    public UUID getId() {
      return id;
    }

    public String getShipper() {
      return shipper;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }
  }

  public static class ShippingReceipt {

    String shipper;
    ArrayList<PurchaseItem> purchaseItems;
    Date shipDate;

    public ShippingReceipt(String shipper, ArrayList<PurchaseItem> purchaseItems, Date shipDate) {
      this.shipper = shipper;
      this.purchaseItems = purchaseItems;
      this.shipDate = shipDate;
    }

    public String getShipper() {
      return shipper;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }

    public Date getShipDate() {
      return shipDate;
    }
  }
}

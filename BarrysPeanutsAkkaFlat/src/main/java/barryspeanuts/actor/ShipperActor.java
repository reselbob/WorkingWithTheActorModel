package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.PurchaseItem;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipperActor extends AbstractBehavior<Object> {

  private static final Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

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
    logger.info(
        "{} is Shipping the purchase using Shipper: {} on {}.\n",
        ShipperActor.class,
        msg.getShipper(),
        shipDate);
    return this;
  }

  public static class ShipmentInfo {

    private final UUID id;
    private final String shipper;
    private final List<PurchaseItem> purchaseItems;

    public ShipmentInfo(String shipper, List<PurchaseItem> purchaseItems) {
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

    public List<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }
  }

  public static class ShippingReceipt {

    String shipper;
    List<PurchaseItem> purchaseItems;
    Date shipDate;

    public ShippingReceipt(String shipper, List<PurchaseItem> purchaseItems, Date shipDate) {
      this.shipper = shipper;
      this.purchaseItems = purchaseItems;
      this.shipDate = shipDate;
    }

    public String getShipper() {
      return shipper;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }

    public Date getShipDate() {
      return shipDate;
    }
  }
}

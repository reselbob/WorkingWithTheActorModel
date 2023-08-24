package barryspeanuts.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class ShoppingCartActor extends AbstractBehavior<Object> {
  private Purchase purchase;

  private ShoppingCartActor(ActorContext<Object> context) {
    super(context);
    this.purchase = new Purchase(UUID.randomUUID());
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(ShoppingCartActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder()
        .onMessage(AddItem.class, this::handleAddItem)
        .onMessage(RemoveItem.class, this::handleRemoveItem)
        .onMessage(ResetCart.class, this::handleResetCart)
        .onMessage(CheckoutCart.class, this::handleCheckoutCart)
        .onMessage(PaymentActor.PaymentInfo.class, this::handlePayment)
        .onMessage(ShipperActor.ShipmentInfo.class, this::handleShipping)
        .build();
  }

  private Behavior<Object> handleAddItem(AddItem msg) {
    this.purchase.add(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleRemoveItem(RemoveItem msg) {
    this.purchase.remove(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleResetCart(ResetCart msg) {
    getContext().getLog().info("ShoppingCart is resetting the cart \n ");
    this.purchase = new Purchase(UUID.randomUUID());
    return this;
  }

  private Behavior<Object> handlePayment(PaymentActor.PaymentInfo msg) {
    // Tell the Payment Actor to pay
    ActorRef<Object> paymentActor = ActorSystem.create(PaymentActor.create(), "paymentActor");
    Customer customer = this.purchase.getPurchaseItems().get(0).getCustomer();
    CreditCard creditCard = msg.getCreditCard();
    BigDecimal totalAmount =
        this.purchase.getPurchaseItems().stream()
            .map(PurchaseItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(
            UUID.randomUUID(), customer, creditCard, totalAmount, this.purchase.getId());
    paymentActor.tell(paymentInfo);
    return this;
  }

  private Behavior<Object> handleShipping(ShipperActor.ShipmentInfo msg) {
    // Tell the Shipper to ship
    ActorRef<Object> shipperActor = ActorSystem.create(ShipperActor.create(), "shipperActor");
    String shipper = msg.getShipper();
    ShipperActor.ShipmentInfo shippingInfo =
        new ShipperActor.ShipmentInfo(UUID.randomUUID(), shipper);
    shippingInfo.setPurchase(this.purchase);
    shipperActor.tell(shippingInfo);
    return this;
  }

  private Behavior<Object> handleCheckoutCart(CheckoutCart msg) {
    String firstName = this.purchase.getPurchaseItems().get(0).getCustomer().getFirstName();
    String lastName = this.purchase.getPurchaseItems().get(0).getCustomer().getLastName();
    getContext()
        .getLog()
        .info(
            "{} {} is starting a checkout of {} items ",
            firstName,
            lastName,
            this.purchase.getPurchaseItems().toArray().length);

    // Tell the CheckOut Actor to check out
    ActorRef<Object> checkoutActor = ActorSystem.create(CheckOutActor.create(), "checkoutActor");
    CheckOutActor.StartCheckout startCheckout =
        new CheckOutActor.StartCheckout(this.purchase.getId());
    checkoutActor.tell(startCheckout);
    return this;
  }

  public static class AddItem {
    private final PurchaseItem purchaseItem;

    public AddItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return purchaseItem;
    }
  }

  public static class RemoveItem {
    private final PurchaseItem purchaseItem;

    public RemoveItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return purchaseItem;
    }
  }

  public static class ResetCart {
    private final Date resetCartDate;

    public ResetCart() {
      this.resetCartDate = new Date();
    }

    public Date getResetCartDate() {
      return resetCartDate;
    }
  }

  public static class CheckoutCart {
    private final Date checkoutCartDate;

    public CheckoutCart() {
      this.checkoutCartDate = new Date();
    }

    public Date getCheckoutCartDate() {
      return checkoutCartDate;
    }
  }
}

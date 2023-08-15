package barryspeanuts.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.helper.MockHelper;
import barryspeanuts.msg.ConfirmationMessage;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCartActor extends AbstractBehavior<Object> {
  private List<PurchaseItem> purchaseItems;

  private ShoppingCartActor(ActorContext<Object> context) {
    super(context);
    this.purchaseItems = new ArrayList<>();
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(ShoppingCartActor::new);
  }

  public static Behavior<Object> behavior() {
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

  private Behavior<Object> handleConfirmationMessage(ConfirmationMessage msg) {
    getContext().getLog().info(msg.getContent());
    return this;
  }

  private Behavior<Object> handleAddItem(AddItem msg) {
    getContext().getLog().info("Adding an Item");
    this.purchaseItems.add(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleRemoveItem(RemoveItem msg) {
    this.purchaseItems.remove(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleResetCart(ResetCart msg) {
    getContext().getLog().info("ShoppingCart is resetting the cart \n ");
    this.purchaseItems = new ArrayList<>();
    return this;
  }

  private Behavior<Object> handlePayment(PaymentActor.PaymentInfo msg) {
    // Tell the Payment Actor to pay
    ActorRef<Object> paymentActor = ActorSystem.create(PaymentActor.create(), "paymentActor");
    Customer customer = this.purchaseItems.get(0).getCustomer();
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard = MockHelper.getCreditCard(firstName, lastName);
    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(customer, creditCard, msg.getPaymentAmount());
    paymentActor.tell(paymentInfo);
    return this;
  }

  private Behavior<Object> handleShipping(ShipperActor.ShipmentInfo msg) {
    // Tell the Shipper to ship
    ActorRef<Object> shipperActor = ActorSystem.create(ShipperActor.create(), "shipperActor");
    String shipper = MockHelper.getShipper();
    ShipperActor.ShipmentInfo shippingInfo =
        new ShipperActor.ShipmentInfo(shipper, this.purchaseItems);
    shipperActor.tell(shippingInfo);
    return this;
  }

  private Behavior<Object> handleCheckoutCart(CheckoutCart msg) {
    String firstName = msg.getPurchaseItems().get(0).getCustomer().getFirstName();
    String lastName = msg.getPurchaseItems().get(0).getCustomer().getLastName();
    getContext().getLog().info(
        "{} {} is starting a checkout of {} items \n",
        firstName, lastName, msg.getPurchaseItems().toArray().length);

    // Tell the CheckOut Actor to check out
    ActorRef<Object> checkoutActor = ActorSystem.create(CheckOutActor.create(), "checkoutActor");
    CheckOutActor.StartCheckout startCheckout = new CheckOutActor.StartCheckout(this.purchaseItems);
    checkoutActor.tell(startCheckout);
    return this;
  }

  public static class AddItem {
    PurchaseItem purchaseItem;

    public AddItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return purchaseItem;
    }
  }

  public static class RemoveItem {
    PurchaseItem purchaseItem;

    public RemoveItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return purchaseItem;
    }
  }

  public static class ResetCart {
    Date resetCartDate;

    public ResetCart() {
      this.resetCartDate = new Date();
    }

    public Date getResetCartDate() {
      return resetCartDate;
    }
  }

  public static class CheckoutCart {
    Date checkoutCartDate;
    List<PurchaseItem> purchaseItems;

    public CheckoutCart(List<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
      this.checkoutCartDate = new Date();
    }

    public List<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }

    public Date getEmptyCartDate() {
      return checkoutCartDate;
    }
  }
}

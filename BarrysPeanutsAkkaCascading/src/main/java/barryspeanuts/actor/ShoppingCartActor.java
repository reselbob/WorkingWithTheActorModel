package barryspeanuts.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.helper.MockHelper;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShoppingCartActor extends AbstractBehavior<Object> {
  Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);
  ArrayList<PurchaseItem> purchaseItems;

  private ShoppingCartActor(ActorContext<Object> context) {
    super(context);
    this.purchaseItems = new ArrayList<PurchaseItem>();
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
        .onMessage(EmptyCart.class, this::handleEmptyCart)
        .onMessage(CheckoutCart.class, this::handleCheckoutCart)
        .build();
  }

  private Behavior<Object> handleAddItem(AddItem msg) {
    this.purchaseItems.add(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleRemoveItem(RemoveItem msg) {
    this.purchaseItems.remove(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleEmptyCart(EmptyCart msg) {
    logger.info(
        "ShoppingCart is emptying the cart of {} items a checkout at {}. \n ",
        this.purchaseItems.toArray().length,
        new Date());
    this.purchaseItems = new ArrayList<>();
    return this;
  }

  private Behavior<Object> handleCheckoutCart(CheckoutCart msg) {
    logger.info(
        "ShoppingCart is starting a checkout of {} items a checkout at {}. \n",
        this.purchaseItems.toArray().length,
        new Date());
    ActorRef<Object> checkoutActor = ActorSystem.create(CheckOutActor.create(), "checkoutActor");
    Customer customer = this.purchaseItems.get(0).getCustomer();
    CreditCard creditCard =
        MockHelper.getCreditCard(customer.getFirstName(), customer.getLastName());
    CheckOutActor.StartCheckout startCheckout =
        new CheckOutActor.StartCheckout(this.purchaseItems, creditCard, customer);

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

  public static class EmptyCart {
    Date emptyCartDate;

    public EmptyCart() {
      this.emptyCartDate = new Date();
    }

    public Date getEmptyCartDate() {
      return emptyCartDate;
    }
  }

  public static class CheckoutCart {
    Date checkoutCartDate;

    public CheckoutCart() {
      this.checkoutCartDate = new Date();
    }

    public Date getEmptyCartDate() {
      return checkoutCartDate;
    }
  }
}

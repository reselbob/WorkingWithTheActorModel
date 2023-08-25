package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.model.Address;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;
import barryspeanuts.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class ShoppingCartActor extends AbstractBehavior<Object> {
  private final List<PurchaseItem> purchaseItems;

  private ShoppingCartActor(ActorContext<Object> context) {
    super(context);
    this.purchaseItems = new ArrayList<>();
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(ShoppingCartActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder()
        .onMessage(AddItems.class, this::handleAddItems)
        .onMessage(RemoveItem.class, this::handleRemoveItem)
        .onMessage(EmptyCart.class, this::handleEmptyCart)
        .onMessage(Checkout.class, this::handleCheckoutCart)
        .build();
  }

  private Behavior<Object> handleAddItems(AddItems msg) {
    this.purchaseItems.addAll(msg.getPurchaseItems());
    return this;
  }

  private Behavior<Object> handleRemoveItem(RemoveItem msg) {
    this.purchaseItems.remove(msg.purchaseItem);
    return this;
  }

  private Behavior<Object> handleEmptyCart(EmptyCart msg) {
    getContext().getLog().info("ShoppingCart is emptying the cart.");
    this.purchaseItems.clear();
    return this;
  }

  private Behavior<Object> handleCheckoutCart(Checkout msg) {
    if (this.purchaseItems.size() == 0) {
      throw new RuntimeException("No purchase items to checkout");
    }

    // Use the customer in the first purchase item as the customer
    Customer customer = purchaseItems.get(0).getCustomer();

    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    getContext()
        .getLog()
        .info(
            "Starting a checkout of {} items for {} {} .",
            purchaseItems.size(),
            firstName,
            lastName);

    // Do the payment
    BigDecimal totalAmount =
        this.purchaseItems.stream()
            .map(PurchaseItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    getContext()
        .getLog()
        .info(
            "{} {} is paying for {} items for the total amount of {} using credit card {}.",
            firstName,
            lastName,
            this.purchaseItems.size(),
            totalAmount,
            msg.getCreditCard().toString());

    // Send a payment receipt to the customer as a fire and forget message
    CustomerActor.PaymentReceipt paymentReceipt =
        new CustomerActor.PaymentReceipt(UUID.randomUUID(), customer, totalAmount);
    ActorSystem<Object> customerActor = ActorSystem.create(CustomerActor.create(), "customerActor");
    customerActor.tell(paymentReceipt);

    // Do the shipping
    getContext()
        .getLog()
        .info(
            "{} is shipping {} purchase items to {} for customer {} {}.",
            msg.getShipper(),
            this.purchaseItems.size(),
            msg.getShippingAddres().toString(),
            firstName,
            lastName);

    // Send a shipping receipt to the customer as a fire and forget message
    CustomerActor.ShippingReceipt shippingReceipt =
        new CustomerActor.ShippingReceipt(
            UUID.randomUUID(), customer, new Vector<>(purchaseItems), msg.getShipper());
    customerActor.tell(shippingReceipt);

    this.purchaseItems.clear();

    return this;
  }

  public static class AddItems {
    private final List<PurchaseItem> purchaseItems;

    public AddItems(List<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }
  }

  public static class RemoveItem {
    private final PurchaseItem purchaseItem;

    public RemoveItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return this.purchaseItem;
    }
  }

  public static class EmptyCart {
    private final Date emptyCartDate;

    public EmptyCart() {
      this.emptyCartDate = new Date();
    }

    public Date getEmptyCartDate() {
      return this.emptyCartDate;
    }
  }

  public static class Checkout {
    private final CreditCard creditCard;
    private final Address billingAddress;
    private final Address shippingAddress;
    private final String shipper;

    public Checkout(
        CreditCard creditCard, Address billingAddress, Address shippingAddress, String shipper) {
      this.creditCard = creditCard;
      this.billingAddress = billingAddress;
      this.shippingAddress = shippingAddress;
      this.shipper = shipper;
    }

    public CreditCard getCreditCard() {
      return this.creditCard;
    }

    public Address getBillingAddress() {
      return this.billingAddress;
    }

    public Address getShippingAddres() {
      return this.shippingAddress;
    }

    public String getShipper() {
      return this.shipper;
    }
  }
}

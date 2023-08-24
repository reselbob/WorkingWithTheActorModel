package barryspeanuts;

import akka.actor.typed.ActorSystem;
import barryspeanuts.actor.PaymentActor;
import barryspeanuts.actor.ShipperActor;
import barryspeanuts.actor.ShoppingCartActor;
import barryspeanuts.model.Address;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;
import barryspeanuts.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);
    logger.info("{} is starting Barry's Gourmet Peanuts.", App.class);

    Address address = new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");

    Customer customer =
        new Customer(
            UUID.randomUUID(), "Barney", "Rubble", "barney@rubble.com", "310 878 9999", address);

    List<PurchaseItem> purchaseItems = new ArrayList<>();

    PurchaseItem purchaseItem1 =
        new PurchaseItem(
            UUID.randomUUID(),
            customer,
            "Barry's Gourmet Peanuts Large",
            5,
            new BigDecimal("1"),
            new BigDecimal("10.99"),
            address,
            address);

    purchaseItems.add(purchaseItem1);

    ShoppingCartActor.AddItem item1 = new ShoppingCartActor.AddItem(purchaseItem1);

    ActorSystem<Object> shoppingCartActor =
        ActorSystem.create(ShoppingCartActor.create(), "shoppingCartActor");

    shoppingCartActor.tell(item1);

    PurchaseItem purchaseItem2 =
        new PurchaseItem(
            UUID.randomUUID(),
            customer,
            "Barry's Gourmet Peanuts Medium",
            3,
            new BigDecimal("2"),
            new BigDecimal("7.99"),
            address,
            address);

    purchaseItems.add(purchaseItem2);

    ShoppingCartActor.AddItem item2 = new ShoppingCartActor.AddItem(purchaseItem2);

    shoppingCartActor.tell(item2);

    PurchaseItem purchaseItem3 =
        new PurchaseItem(
            UUID.randomUUID(),
            customer,
            "Barry's Gourmet Peanuts Small",
            1,
            new BigDecimal("3"),
            new BigDecimal("4.99"),
            address,
            address);

    purchaseItems.add(purchaseItem3);

    ShoppingCartActor.AddItem item3 = new ShoppingCartActor.AddItem(purchaseItem3);

    shoppingCartActor.tell(item3);

    // Checkout
    ShoppingCartActor.CheckoutCart checkout = new ShoppingCartActor.CheckoutCart();
    shoppingCartActor.tell(checkout);

    // Get  the credit card
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard =
        new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);

    BigDecimal purchaseTotal = new BigDecimal("0");
    for (PurchaseItem purchaseItem : purchaseItems) {
      purchaseTotal = purchaseTotal.add(purchaseItem.getTotal());
    }

    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(UUID.randomUUID(), customer, creditCard, purchaseTotal);
    shoppingCartActor.tell(paymentInfo);

    // Ship

    ShipperActor.ShipmentInfo shipmentInfo =
        new ShipperActor.ShipmentInfo(UUID.randomUUID(), purchaseItems, "FEDEX");
    shoppingCartActor.tell(shipmentInfo);

    // Reset Cart
    ShoppingCartActor.EmptyCart resetCart = new ShoppingCartActor.EmptyCart();
    shoppingCartActor.tell(resetCart);
  }
}

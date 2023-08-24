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
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);
    logger.info("{} is starting Barry's Gourmet Peanuts", App.class);

    Address address = new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");

    Customer customer =
        new Customer(
            UUID.randomUUID(), "Barney", "Rubble", "barney@rubble.com", "310 878 9999", address);
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

    ShoppingCartActor.AddItem item3 = new ShoppingCartActor.AddItem(purchaseItem3);

    shoppingCartActor.tell(item3);

    // Checkout
    ShoppingCartActor.CheckoutCart checkout = new ShoppingCartActor.CheckoutCart();
    shoppingCartActor.tell(checkout);

    // Pay
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard =
        new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);
    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(UUID.randomUUID(), customer, creditCard);
    shoppingCartActor.tell(paymentInfo);

    // Ship

    ShipperActor.ShipmentInfo shipmentInfo =
        new ShipperActor.ShipmentInfo(UUID.randomUUID(), "FEDEX");
    shoppingCartActor.tell(shipmentInfo);

    // Reset Cart
    ShoppingCartActor.ResetCart resetCart = new ShoppingCartActor.ResetCart();
    shoppingCartActor.tell(resetCart);
  }
}

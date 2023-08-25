package barryspeanuts;

import akka.actor.typed.ActorSystem;
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
    logger.info("Starting Barry's Gourmet Peanuts.");

    Address address = new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");

    Customer customer =
        new Customer(
            UUID.randomUUID(), "Barney", "Rubble", "barney@rubble.com", "310 878 9999", address);

    // Create some purchase items to add to the shopping cart
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

    // Create the shopping cart actor
    ActorSystem<Object> shoppingCartActor =
        ActorSystem.create(ShoppingCartActor.create(), "shoppingCartActor");

    // Create an instance of the AddItems behavior
    ShoppingCartActor.AddItems shoppingCartItems = new ShoppingCartActor.AddItems(purchaseItems);

    // Pass the AddItems message on to the ShoppingCartActor
    shoppingCartActor.tell(shoppingCartItems);

    // Prepare for checkout by creating the Credit Card as well as Billing and Shipping Addresses

    // Get  the credit card
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard =
        new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);

    // Use the customer's base address for billing and shipping
    Address billingAddress = customer.getAddress();
    Address shippingAddress = customer.getAddress();
    String shipper = "FEDEX";

    // Checkout
    ShoppingCartActor.Checkout checkout =
        new ShoppingCartActor.Checkout(creditCard, billingAddress, shippingAddress, shipper);
    shoppingCartActor.tell(checkout);
  }
}

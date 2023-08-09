package barryspeanuts;

import akka.actor.typed.ActorSystem;
import barryspeanuts.actor.PaymentActor;
import barryspeanuts.actor.ShipperActor;
import barryspeanuts.actor.ShoppingCartActor;
import barryspeanuts.helper.MockHelper;
import barryspeanuts.msg.Address;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void main(String[] args) throws InterruptedException, TimeoutException {
    Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);
    logger.info("{} is starting Barry's Gourmet Peanuts", App.class);

    Customer customer = MockHelper.getCustomer();
    Address address = MockHelper.getAddress();

    ArrayList<PurchaseItem> purchaseItems = new ArrayList<>();
    PurchaseItem purchase =
        new PurchaseItem(customer, "Barry's Gourmet Peanuts", 5, 1, 10.99, address, address);

    ShoppingCartActor.AddItem item = new ShoppingCartActor.AddItem(purchase);

    ActorSystem<Object> shoppingCartActor =
        ActorSystem.create(ShoppingCartActor.create(), "shoppingCartActor");
    purchaseItems.add(purchase);
    shoppingCartActor.tell(item);

    purchaseItems.add(purchase);
    shoppingCartActor.tell(item);

    purchaseItems.add(purchase);
    shoppingCartActor.tell(item);

    // Checkout
    ShoppingCartActor.CheckoutCart checkout = new ShoppingCartActor.CheckoutCart();
    shoppingCartActor.tell(checkout);

    // Pay
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard = MockHelper.getCreditCard(firstName, lastName);
    double totalAmount = purchaseItems.stream().mapToDouble(PurchaseItem::getTotal).sum();
    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(customer, creditCard, totalAmount);
    shoppingCartActor.tell(paymentInfo);

    // Ship
    ShipperActor.ShipmentInfo shipmentInfo =
        new ShipperActor.ShipmentInfo(MockHelper.getShipper(), purchaseItems);
    shoppingCartActor.tell(shipmentInfo);

    // Empty Cart
    ShoppingCartActor.EmptyCart emptyCart = new ShoppingCartActor.EmptyCart();
    shoppingCartActor.tell(emptyCart);
  }
}

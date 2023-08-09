package barryspeanuts;

import akka.actor.typed.ActorSystem;
import barryspeanuts.actor.ShoppingCartActor;
import barryspeanuts.helper.MockHelper;
import barryspeanuts.msg.Address;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void main(String[] args) throws InterruptedException, TimeoutException {
    Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);
    logger.info("{} is starting Barry's Gourmet Peanuts", App.class);
    Customer customer = MockHelper.getCustomer();
    Address address = MockHelper.getAddress();

    PurchaseItem purchase =
        new PurchaseItem(customer, "Barry's Gourmet Peanuts", 5, 1, 10.99, address, address);
    ShoppingCartActor.AddItem item = new ShoppingCartActor.AddItem(purchase);
    ActorSystem<Object> shoppingCartActor =
        ActorSystem.create(ShoppingCartActor.create(), "shoppingCartActor");
    shoppingCartActor.tell(item);
    shoppingCartActor.tell(item);
    shoppingCartActor.tell(item);
    ShoppingCartActor.CheckoutCart checkout = new ShoppingCartActor.CheckoutCart();
    shoppingCartActor.tell(checkout);
    // BEWARE!!! Calling EmptyCart() here can cause race conditions
    ShoppingCartActor.EmptyCart emptyCart = new ShoppingCartActor.EmptyCart();
    shoppingCartActor.tell(emptyCart);
  }
}

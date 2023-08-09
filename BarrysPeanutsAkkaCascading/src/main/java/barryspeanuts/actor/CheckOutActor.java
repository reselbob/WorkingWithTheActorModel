package barryspeanuts.actor;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.CreditCard;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckOutActor extends AbstractBehavior<Object> {
  Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

  private CheckOutActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(CheckOutActor::new);
  }

  public static Behavior<Object> behavior() {
    return Behaviors.setup(CheckOutActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder().onMessage(StartCheckout.class, this::handleStartCheckout).build();
  }

  private Behavior<Object> handleStartCheckout(StartCheckout msg) {
    logger.info("{} is Checking Out a purchase and preparing a payment", CheckOutActor.class);
    PaymentActor.PaymentInfo paymentInfo =
        new PaymentActor.PaymentInfo(
            msg.getCustomer(), msg.getCreditCard(), msg.getPurchaseItems());
    ActorSystem<Object> paymentActor = ActorSystem.create(PaymentActor.create(), "paymentActor");
    // send payment to the PaymentActor
    paymentActor.tell(paymentInfo);
    return this;
  }

  public static class StartCheckout {
    ArrayList<PurchaseItem> purchaseItems;
    CreditCard creditCard;
    Customer customer;

    public StartCheckout(
        ArrayList<PurchaseItem> purchaseItems, CreditCard creditCard, Customer customer) {
      this.purchaseItems = purchaseItems;
      this.creditCard = creditCard;
      this.customer = customer;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }

    public CreditCard getCreditCard() {
      return creditCard;
    }

    public Customer getCustomer() {
      return customer;
    }
  }
}

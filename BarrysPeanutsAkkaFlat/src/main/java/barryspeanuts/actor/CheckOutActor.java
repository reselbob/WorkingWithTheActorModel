package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.PurchaseItem;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckOutActor extends AbstractBehavior<Object> {
  private static final Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

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
    logger.info("{} has has started Checkout\n", CheckOutActor.class);
    return this;
  }

  public static class StartCheckout {
    ArrayList<PurchaseItem> purchaseItems;

    public StartCheckout(ArrayList<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
    }

    public ArrayList<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }
  }
}

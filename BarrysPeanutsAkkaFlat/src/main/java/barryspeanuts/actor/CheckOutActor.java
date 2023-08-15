package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import barryspeanuts.msg.Customer;
import barryspeanuts.msg.PurchaseItem;
import java.util.List;

public class CheckOutActor extends AbstractBehavior<Object> {
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
    Customer customer = msg.getPurchaseItems().get(0).getCustomer();
    getContext()
        .getLog()
        .info(
            "{} {} has has started Checkout of {} items.\n",
            customer.getFirstName(),
            customer.getLastName(),
            msg.purchaseItems.toArray().length);
    return this;
  }

  public static class StartCheckout {
    private final List<PurchaseItem> purchaseItems;

    public StartCheckout(List<PurchaseItem> purchaseItems) {
      this.purchaseItems = purchaseItems;
    }

    public List<PurchaseItem> getPurchaseItems() {
      return purchaseItems;
    }
  }
}

package barryspeanuts.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.util.UUID;

public class CheckOutActor extends AbstractBehavior<Object> {
  private CheckOutActor(ActorContext<Object> context) {
    super(context);
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(CheckOutActor::new);
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder().onMessage(StartCheckout.class, this::handleStartCheckout).build();
  }

  private Behavior<Object> handleStartCheckout(StartCheckout msg) {
    getContext()
        .getLog()
        .info(
            "TODO: Getting purchase state for PurchaseId {} from data store and denote that checkout has " +
                    "started or relying upon Akka event sourcing.",
            msg.purchaseId);

    getContext().getLog().info("Starting checkout for PurchaseId {}.", msg.purchaseId);

    getContext()
        .getLog()
        .info(
            "TODO: Saving purchase state for PurchaseId {} from data store and denote that checkout has started." +
                    " or rely upon Akka Event Sourcing",
            msg.purchaseId);
    return this;
  }

  public static class StartCheckout {
    private final UUID purchaseId;

    public StartCheckout(UUID purchaseId) {
      this.purchaseId = purchaseId;
    }
  }
}

package barryspeanuts.msg;

import akka.actor.typed.ActorRef;

public class ConfirmationMessage {
  final String content;
  final akka.actor.typed.ActorRef<Confirmation> replyTo;

  public ConfirmationMessage(String content, akka.actor.typed.ActorRef<Confirmation> replyTo) {
    this.content = content;
    this.replyTo = replyTo;
  }

  public String getContent() {
    return content;
  }

  public ActorRef<Confirmation> getReplyTo() {
    return replyTo;
  }
}

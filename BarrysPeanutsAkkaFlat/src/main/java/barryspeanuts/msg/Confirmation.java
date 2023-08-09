package barryspeanuts.msg;

public class Confirmation {
  final String confirmationMsg;

  public Confirmation(String confirmationMsg) {
    this.confirmationMsg = confirmationMsg;
  }

  public String getConfirmationMsg() {
    return confirmationMsg;
  }
}

package barryspeanuts.model;

import java.util.Date;
import java.util.UUID;

public class CheckOutReceipt {
  UUID id;
  Date confirmationDate;
  Purchase purchase;

  ;

  public CheckOutReceipt() {}

  public CheckOutReceipt(Purchase purchase, Date confirmationDate) {
    this.id = UUID.randomUUID();
    this.confirmationDate = confirmationDate;
    this.purchase = purchase;
  }

  public UUID getId() {
    return id;
  }

  public Date getConfirmationDate() {
    return this.confirmationDate;
  }

  public Purchase getPurchase() {
    return this.purchase;
  }
}

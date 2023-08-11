package barryspeanuts.model;

import java.util.Date;
import java.util.UUID;

public class CheckOutReceipt {
  private UUID id;
  private Date confirmationDate;
  private Purchase purchase;

  /*
  Add a parameterless constructor and setters to avoid complaints
  by the serializer.
  */
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

  public void setId(UUID id) {
    this.id = id;
  }
}

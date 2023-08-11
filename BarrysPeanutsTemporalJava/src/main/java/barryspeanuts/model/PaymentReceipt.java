package barryspeanuts.model;

import java.util.Date;
import java.util.UUID;

public class PaymentReceipt {

  private UUID id;
  private Purchase purchase;
  private Date paymentDate;
  private CreditCard creditCard;
  private UUID transactionId;

  /*
  Add a parameterless constructor and setters to avoid complaints
  by the serializer.
  */
  public PaymentReceipt() {}

  public PaymentReceipt(
      Purchase purchase, Date paymentDate, CreditCard creditCard, UUID transactionId) {
    this.id = UUID.randomUUID();
    this.purchase = purchase;
    this.paymentDate = paymentDate;
    this.creditCard = creditCard;
    this.transactionId = transactionId;
  }

  public UUID getId() {
    return this.id;
  }

  public Purchase getPurchase() {
    return this.purchase;
  }

  public Date getPaymentDate() {
    return this.paymentDate;
  }

  public CreditCard getCreditCard() {
    return this.creditCard;
  }

  public UUID getTransactionId() {
    return this.transactionId;
  }
}

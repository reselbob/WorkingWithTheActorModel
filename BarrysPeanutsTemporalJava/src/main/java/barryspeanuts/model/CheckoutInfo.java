package barryspeanuts.model;

public class CheckoutInfo {
  private CreditCard creditCard;
  private Address billingAddress;
  private Address shippingAddress;
  private String shipper;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public CheckoutInfo() {}

  public CheckoutInfo(
      CreditCard creditCard, Address billingAddress, Address shippingAddress, String shipper) {
    this.creditCard = creditCard;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
    this.shipper = shipper;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public String getShipper() {
    return shipper;
  }
}

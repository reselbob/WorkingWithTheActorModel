package barryspeanuts.msg;

import java.util.Date;
import java.util.UUID;

public class PurchaseItem {

  private final UUID id;
  private final Customer customer;
  private final String description;
  private final int packageSize;
  private final int quantity;
  private final double price;
  private final Address billingAddress;
  private final Address shippingAddress;
  private Date shipDate;

  public PurchaseItem(
      Customer customer,
      String description,
      int packageSize,
      int quantity,
      double price,
      Address billingAddress,
      Address shippingAddress) {

    this.id = UUID.randomUUID();
    this.description = description;
    this.customer = customer;
    this.packageSize = packageSize;
    this.quantity = quantity;
    this.price = price;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
    this.shipDate = null;
  }

  public UUID getId() {
    return this.id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public int getPackageSize() {
    return packageSize;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  public double getTotal() {
    return this.quantity * this.price;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  public String getDescription() {
    return description;
  }
}

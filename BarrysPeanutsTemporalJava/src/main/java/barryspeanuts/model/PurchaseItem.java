package barryspeanuts.model;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class PurchaseItem {
  UUID id;
  Customer customer;
  String description;
  int packageSize;
  double price;
  int quantity;
  double total;
  Optional<Address> billingAddress;
  Optional<Address> shippingAddress;
  Date purchaseDate;

  public PurchaseItem() {}

  public PurchaseItem(
      Customer customer, String description, int packageSize, double price, int quantity) {
    this.id = UUID.randomUUID();
    this.description = description;
    this.customer = customer;
    this.packageSize = packageSize;
    this.price = price;
    this.quantity = quantity;
  }

  public UUID getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getDescription() {
    return description;
  }

  public int getPackageSize() {
    return packageSize;
  }

  public double getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getTotal() {
    return total;
  }

  public Optional<Address> getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Optional<Address> billingAddress) {
    this.billingAddress = billingAddress;
  }

  public Optional<Address> getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Optional<Address> shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}

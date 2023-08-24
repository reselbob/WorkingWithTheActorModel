package barryspeanuts.model;

import java.math.BigDecimal;
import java.util.UUID;

public class PurchaseItem {
  private UUID id;
  private Customer customer;
  private String description;
  private int packageSize;
  private BigDecimal price;
  private BigDecimal quantity;

  private BigDecimal total;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public PurchaseItem() {}

  public PurchaseItem(
      UUID id,
      Customer customer,
      String description,
      int packageSize,
      BigDecimal price,
      BigDecimal quantity) {
    this.id = id;
    this.description = description;
    this.customer = customer;
    this.packageSize = packageSize;
    this.price = price;
    this.quantity = quantity;
    this.total = quantity.multiply(price);
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

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getTotal() {
    return this.total;
  }
}

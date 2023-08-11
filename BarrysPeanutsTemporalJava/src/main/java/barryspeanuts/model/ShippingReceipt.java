package barryspeanuts.model;

import java.util.Date;
import java.util.UUID;

public class ShippingReceipt {

  UUID id;
  Date shipDate;
  String shipper;

  /*
  Add a parameterless constructor and setters to avoid complaints
  by the serializer.
  */
  public ShippingReceipt() {}

  public ShippingReceipt(Purchase purchase, String shipper) {
    this.id = UUID.randomUUID();
    this.shipDate = new Date();
    this.shipper = shipper;
  }

  public UUID getId() {
    return this.id;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public String getShipper() {
    return shipper;
  }
}

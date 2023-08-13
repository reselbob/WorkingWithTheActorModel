package barryspeanuts.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Purchase {

  UUID id;
  List<PurchaseItem> purchaseItems;
  Date purchaseDate;

  /*
  Add a parameterless constructor and setters to avoid complaints
  by the serializer.
  */
  public Purchase() {}

  public Purchase(List<PurchaseItem> purchaseItems) {
    this.id = UUID.randomUUID();
    this.purchaseItems = purchaseItems;
    this.purchaseDate = new Date();
  }

  public List<PurchaseItem> getPurchaseItems() {
    return purchaseItems;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public UUID getId() {
    return id;
  }

}

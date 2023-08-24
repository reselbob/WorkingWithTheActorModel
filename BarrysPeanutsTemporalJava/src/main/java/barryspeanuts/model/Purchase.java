package barryspeanuts.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Purchase {
  private UUID id;
  private List<PurchaseItem> purchaseItems;
  private Date purchaseDate;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public Purchase() {}

  public Purchase(UUID id, List<PurchaseItem> purchaseItems) {
    this.id = id;
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

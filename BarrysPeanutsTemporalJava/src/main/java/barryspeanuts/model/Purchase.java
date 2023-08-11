package barryspeanuts.model;

import java.util.Date;
import java.util.List;

public class Purchase {

  String id;
  List<PurchaseItem> purchaseItems;
  Date purchaseDate;

  /*
  Add a parameterless constructor and setters to avoid complaints
  by the serializer.
  */
  public Purchase() {}

  public Purchase(List<PurchaseItem> purchaseItems, Date purchaseDate) {
    this.purchaseItems = purchaseItems;
    this.purchaseDate = purchaseDate;
  }

  public List<PurchaseItem> getPurchaseItems() {
    return purchaseItems;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }
}

package barryspeanuts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Purchase {
  private final UUID id;
  List<PurchaseItem> purchaseItems;

  public Purchase(UUID id) {
    this.id = id;
    this.purchaseItems = new ArrayList<>();
  }

  public void add(PurchaseItem purchaseItem) {
    this.purchaseItems.add(purchaseItem);
  }

  public void remove(PurchaseItem purchaseItem) {
    this.purchaseItems.remove(purchaseItem);
  }

  public UUID getId() {
    return id;
  }

  public List<PurchaseItem> getPurchaseItems() {
    return purchaseItems;
  }
}

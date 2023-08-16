package barryspeanuts.msg;

import barryspeanuts.model.PurchaseItem;

import java.util.List;

public class CheckOutItems {
  private final List<PurchaseItem> items;

  public CheckOutItems(List<PurchaseItem> items) {
    this.items = items;
  }

  public List<PurchaseItem> getItems() {
    return items;
  }
}

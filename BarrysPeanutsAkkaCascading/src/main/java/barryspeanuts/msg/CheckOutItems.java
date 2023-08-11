package barryspeanuts.msg;

import java.util.List;

public class CheckOutItems {
  public List<PurchaseItem> items;

  public CheckOutItems(List<PurchaseItem> items) {
    this.items = items;
  }

  public List<PurchaseItem> getItems() {
    return items;
  }
}

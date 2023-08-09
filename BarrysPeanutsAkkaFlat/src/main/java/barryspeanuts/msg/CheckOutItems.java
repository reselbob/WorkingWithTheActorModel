package barryspeanuts.msg;

import java.util.Vector;

public class CheckOutItems {
  public Vector<PurchaseItem> items;

  public CheckOutItems(Vector<PurchaseItem> items) {
    this.items = items;
  }

  public Vector<PurchaseItem> getItems() {
    return items;
  }
}

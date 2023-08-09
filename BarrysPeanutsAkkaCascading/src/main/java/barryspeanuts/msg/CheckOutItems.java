package barryspeanuts.msg;

import java.util.ArrayList;

public class CheckOutItems {
  public ArrayList<PurchaseItem> items;

  public CheckOutItems(ArrayList<PurchaseItem> items) {
    this.items = items;
  }

  public ArrayList<PurchaseItem> getItems() {
    return items;
  }
}

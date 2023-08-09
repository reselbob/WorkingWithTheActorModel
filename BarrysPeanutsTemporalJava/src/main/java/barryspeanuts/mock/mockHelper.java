package barryspeanuts.mock;

import barryspeanuts.model.Address;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;
import barryspeanuts.model.PurchaseItem;

public class mockHelper {
  public static Address getAddress() {
    return new Address(
        "1600 Pennsylvania Avenue NW.", "West Wing", "Washington", "D.C.", "20500", "USA");
  }

  public static Customer getCustomer() {
    return new Customer("Josiah", "Bartlet", "prez@whitehouse.gove", "202 456 1414", getAddress());
  }

  public static PurchaseItem getPurchaseItem() {
    return new PurchaseItem(getCustomer(), "Deluxe Peanuts:", 3, 12.99, 5);
  }

  public static CreditCard getCreditCard(String firstName, String lastName) {
    String fullName = String.format("%s %s", firstName, lastName);
    return new CreditCard(fullName, "11112222333334444", 8, 2026, 999);
  }
}

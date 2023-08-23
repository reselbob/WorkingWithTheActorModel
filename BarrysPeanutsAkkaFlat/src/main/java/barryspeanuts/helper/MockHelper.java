package barryspeanuts.helper;

import barryspeanuts.model.Address;
import barryspeanuts.model.CreditCard;
import barryspeanuts.model.Customer;

import java.util.UUID;

public class MockHelper {

  public static CreditCard getCreditCard(String firstName, String lastName) {
    return new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);
  }
  ;

  public static Customer getCustomer() {
    return new Customer(UUID.randomUUID(),"Barney", "Rubble", "barney@rubble.com", "310 878 9999", getAddress());
  }

  public static Address getAddress() {
    return new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");
  }

  public static String getShipper() {
    return "FEDEX";
  }
}

package barryspeanuts.msg;

import java.util.UUID;

public class Customer {
  private final UUID id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String phone;
  private final Address address;

  public Customer(String firstName, String lastName, String email, String phone, Address address) {
    this.id = UUID.randomUUID();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  public UUID getId() {
    return this.id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public Address getAddress() {
    return address;
  }
}

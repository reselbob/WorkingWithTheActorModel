package barryspeanuts.model;

public class CreditCard {
  String fullName;
  String number;
  int expirationMonth;
  int expirationYear;
  int securityCode;

  public CreditCard() {}

  public CreditCard(
      String fullName, String number, int expirationMonth, int expirationYear, int securityCode) {
    this.fullName = fullName;
    this.number = number;
    this.expirationMonth = expirationMonth;
    this.expirationYear = expirationYear;
    this.securityCode = securityCode;
  }

  public String getFullName() {
    return fullName;
  }

  public String getNumber() {
    return number;
  }

  public int getExpirationMonth() {
    return expirationMonth;
  }

  public int getExpirationYear() {
    return expirationYear;
  }

  public int getSecurityCode() {
    return securityCode;
  }
}

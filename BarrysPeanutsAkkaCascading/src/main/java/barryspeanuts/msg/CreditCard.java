package barryspeanuts.msg;

public class CreditCard {
  private String nameOnCard;
  private String creditCardNumber;
  private Integer expirationMonth;
  private Integer expirationYear;
  private Integer securityCode;

  public CreditCard(
      String nameOnCard,
      String creditCardNumber,
      Integer expirationMonth,
      Integer expirationYear,
      Integer securityCode) {
    this.nameOnCard = nameOnCard;
    this.creditCardNumber = creditCardNumber;
    this.expirationMonth = expirationMonth;
    this.expirationYear = expirationYear;
    this.securityCode = securityCode;
  }

  public String getNameOnCard() {
    return nameOnCard;
  }

  public void setNameOnCard(String nameOnCard) {
    this.nameOnCard = nameOnCard;
  }

  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(String creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public Integer getExpirationMonth() {
    return expirationMonth;
  }

  public void setExpirationMonth(Integer expirationMonth) {
    this.expirationMonth = expirationMonth;
  }

  public Integer getExpirationYear() {
    return expirationYear;
  }

  public void setExpirationYear(Integer expirationYear) {
    this.expirationYear = expirationYear;
  }

  public Integer getSecurityCode() {
    return securityCode;
  }

  public void setSecurityCode(Integer securityCode) {
    this.securityCode = securityCode;
  }
}

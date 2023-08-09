package barryspeanuts.msg;

public class Address {
  private String address1;
  private String address2;
  private String city;
  private String stateProvince;
  private String zipRegionCode;
  private String countryCode;

  public Address(
      String address1,
      String address2,
      String city,
      String stateProvince,
      String zipRegionCode,
      String countryCode) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.stateProvince = stateProvince;
    this.zipRegionCode = zipRegionCode;
    this.countryCode = countryCode;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateProvince() {
    return stateProvince;
  }

  public void setStateProvince(String stateProvince) {
    this.stateProvince = stateProvince;
  }

  public String getZipRegionCode() {
    return zipRegionCode;
  }

  public void setZipRegionCode(String zipRegionCode) {
    this.zipRegionCode = zipRegionCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }
}

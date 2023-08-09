package barryspeanuts.model;

public class Address {
  String address1;
  String address2;
  String city;
  String stateProvince;
  String zipRegionCode;
  String country;

  public Address() {}

  public Address(
      String address1,
      String address2,
      String city,
      String stateProvince,
      String zipRegionCode,
      String country) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.stateProvince = stateProvince;
    this.zipRegionCode = zipRegionCode;
    this.country = country;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}

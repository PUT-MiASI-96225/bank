package pl.mefiu.bank;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class Address {

    public Address(String city, String postalCode, String street, String apartmentNumber, String state) {
        setCity(city);
        setPostalCode(postalCode);
        setStreet(street);
        setApartmentNumber(apartmentNumber);
        setState(state);
    }

    public Address() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if((city != null) && (city.isEmpty())) {
            throw new IllegalArgumentException("city cannot be empty!");
        }
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if((postalCode != null) && (postalCode.isEmpty())) {
            throw new IllegalArgumentException("postalCode cannot be empty!");
        }
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if((street != null) && (street.isEmpty())) {
            throw new IllegalArgumentException("street cannot be empty!");
        }
        this.street = street;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        if((apartmentNumber != null) && (apartmentNumber.isEmpty())) {
            throw new IllegalArgumentException("apartmentNumber cannot be empty!");
        }
        this.apartmentNumber = apartmentNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if((state != null) && (state.isEmpty())) {
            throw new IllegalArgumentException("state cannot be empty!");
        }
        this.state = state;
    }

    @Column(name = "city", nullable = false, unique = false)
    private String city;

    @Column(name = "postal_code", nullable = false, unique = false)
    private String postalCode;

    @Column(name = "street", nullable = false, unique = false)
    private String street;

    @Column(name = "apartment_number", nullable = false, unique = false)
    private String apartmentNumber;

    @Column(name = "state", nullable = false, unique = false)
    private String state;

}
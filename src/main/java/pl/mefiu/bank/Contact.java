package pl.mefiu.bank;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class Contact {

    public Contact(String email, String phoneNumber) {
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public Contact() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if((email != null) && (email.isEmpty())) {
            throw new IllegalArgumentException("email cannot be empty!");
        }
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if((phoneNumber != null) && (phoneNumber.isEmpty())) {
            throw new IllegalArgumentException("phoneNumber cannot be empty!");
        }
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;
}
package pl.mefiu.bank;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
@Entity
@Table(name = "customer")
public final class Customer {

    public Customer(String firstName, String secondName, Date dateOfBirth, String signature, String pin, String password, Address address, Contact contact) {
        setFirstName(firstName);
        setSecondName(secondName);
        setDateOfBirth(dateOfBirth);
        setSignature(signature);
        setPin(pin);
        setPassword(password);
        setAddress(address);
        setContact(contact);
        setAccounts(new HashSet<>());
        setCustomerEmployers(new HashSet<>());
    }

    protected Customer() {

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth.getTime());
    }

    public String getSignature() {
        return signature;
    }

    public String getPin() {
        return pin;
    }

    public String getPassword() {
        return password;
    }

    public Address getAddress() {
        return address;
    }

    public Contact getContact() {
        return contact;
    }

    public Set<Account> getAccounts() {
        return Collections.unmodifiableSet(accounts);
    }

    public Set<CustomerEmployer> getCustomerEmployers() {
        return Collections.unmodifiableSet(customerEmployers);
    }

    public void setFirstName(String firstName) {
        if((firstName != null) && (firstName.isEmpty())) {
            throw new IllegalArgumentException("firstName cannot be empty!");
        }
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        if((secondName != null) && (secondName.isEmpty())) {
            throw new IllegalArgumentException("secondName cannot be empty!");
        }
        this.secondName = secondName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setSignature(String signature) {
        if((signature != null) && (signature.isEmpty())) {
            throw new IllegalArgumentException("signature cannot be empty!");
        }
        this.signature = signature;
    }

    public void setPin(String pin) {
        if((pin != null) && (pin.isEmpty())) {
            throw new IllegalArgumentException("pin cannot be empty!");
        }
        this.pin = pin;
    }

    public void setPassword(String password) {
        if((password != null) && (password.isEmpty())) {
            throw new IllegalArgumentException("password cannot be empty!");
        }
        this.password = password;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    private void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    private void setCustomerEmployers(Set<CustomerEmployer> customerEmployers) {
        this.customerEmployers = customerEmployers;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addCustomerEmployer(CustomerEmployer customerEmployer) {
        customerEmployers.add(customerEmployer);
    }

    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "first_name", nullable = false, unique = false)
    private String firstName;

    @Column(name = "second_name", nullable = false, unique = false)
    private String secondName;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = false, unique = false)
    private Date dateOfBirth;

    @Column(name = "signature", nullable = false, unique = true)
    private String signature;

    @Column(name = "pin", nullable = false, unique = true)
    private String pin;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Embedded
    private Address address;
    
    @Embedded
    private Contact contact;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Account> accounts;

    @ManyToMany(mappedBy = "customers", fetch = FetchType.EAGER)
    private Set<CustomerEmployer> customerEmployers;

}

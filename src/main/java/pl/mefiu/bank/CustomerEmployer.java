package pl.mefiu.bank;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
@Entity
@Table(name = "customer_employer")
public final class CustomerEmployer {

    public CustomerEmployer(Date expiresAt, Double salary, Contact contact, Address address, String name, Customer customer) {
        setExpiresAt(expiresAt);
        setSalary(salary);
        setContact(contact);
        setAddress(address);
        setName(name);
        setCustomers(new HashSet<>());
        addCustomer(customer);
    }

    protected CustomerEmployer() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Contact getContact() {
        return contact;
    }

    public Double getSalary() {
        return salary;
    }

    public Date getExpiresAt() {
        return new Date(expiresAt.getTime());
    }

    public Set<Customer> getCustomers() {
        return Collections.unmodifiableSet(customers);
    }

    public void setName(String name) {
        if((name != null) && (name.isEmpty())) {
            throw new IllegalArgumentException("name cannot be empty!");
        }
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setSalary(Double salary) {
        if(salary != null) {
            if(salary < 0) {
                throw new IllegalArgumentException("bad salary!");
            }
        }
        this.salary = salary;
    }

    public void setExpiresAt(Date expiresAt) {
        if(expiresAt != null) {
            if(expiresAt.before(new Date())) {
                throw new IllegalArgumentException("bad date!");
            }
        }
        this.expiresAt = expiresAt;
    }

    private void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private Contact contact;

    @Column(name = "salary", nullable = false, unique = false)
    private Double salary;

    @Temporal(TemporalType.DATE)
    @Column(name = "expires_at", nullable = true, unique = false)
    private Date expiresAt;

    @ManyToMany
    private Set<Customer> customers;

}

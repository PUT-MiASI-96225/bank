package pl.mefiu.bank;

import org.hibernate.Session;

import java.util.Date;

public abstract class AbstractFactory {

    public abstract Customer createCustomer(String firstName, String secondName, Date dateOfBirth, String signature,
                                            String pin, String city, String postalCode, String street,
                                            String apartmentNumber, String state, String email, String phoneNumber,
                                            Session session);

    public abstract Employee createOrdinaryEmployee(String name, Date expiresAt, Session session);

    public abstract Employee createAdminEmployee(String name, Date expiresAt, Session session);

    public abstract Account createAccount(Date expiresAt, Double balance, String iban, Customer customer,
                                          Session session);

    public abstract Account createDebitAccount(Account account);

    public abstract CustomerEmployer createCustomerEmployer(Date expiresAt, Double salary, String city,
                                                            String postalCode, String street, String apartmentNumber,
                                                            String state, String email, String phoneNumber, String name,
                                                            Customer customer, Session session);

}

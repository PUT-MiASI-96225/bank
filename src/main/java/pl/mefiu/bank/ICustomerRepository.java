package pl.mefiu.bank;

import java.util.Date;
import java.util.List;

public interface ICustomerRepository {

    List<Customer> customersByFirstName(final String firstName);

    List<Customer> customersBySecondName(final String secondName);

    List<Customer> customersByDateOfBirth(final Date dateOfBirth);

    List<Customer> customersByCity(final String city);

    List<Customer> customersByPostalCode(final String postalCode);

    List<Customer> customersByState(final String state);

    List<Customer> fetchAll();

    Customer customerBySignature(final String signature);

    Customer customerByPin(final String pin);

    Customer customerByPassword(final String password);

    Customer customerByEmail(String email);

    Customer customerByPhoneNumber(String phoneNumber);

    Customer customerByAccount(final Account account);

}

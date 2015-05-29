package pl.mefiu.bank;

import java.util.Date;
import java.util.List;

public final class CustomerRepository extends Repository<Customer> implements ICustomerRepository {

    @Override
    public List<Customer> customersByFirstName(final String firstName) {
        if((firstName == null) || (firstName.isEmpty())) {
            throw new IllegalArgumentException("firstName cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = firstName;
        return read("from Customer customer where customer.firstName = ?", params);
    }

    @Override
    public List<Customer> customersBySecondName(final String secondName) {
        if((secondName == null) || (secondName.isEmpty())) {
            throw new IllegalArgumentException("secondName cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = secondName;
        return read("from Customer customer where customer.secondName = ?", params);
    }

    @Override
    public List<Customer> customersByDateOfBirth(final Date dateOfBirth) {
        if(dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth cannot be null!");
        }
        Object[] params = new Object[1];
        params[0] = dateOfBirth;
        return read("from Customer customer where customer.dateOfBirth = ?", params);
    }

    @Override
    public List<Customer> customersByCity(final String city) {
        if((city == null) || (city.isEmpty())) {
            throw new IllegalArgumentException("city cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = city;
        return read("from Customer customer where customer.address.city = ?", params);
    }

    @Override
    public List<Customer> customersByPostalCode(final String postalCode) {
        if((postalCode == null) || (postalCode.isEmpty())) {
            throw new IllegalArgumentException("postalCode cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = postalCode;
        return read("from Customer customer where customer.address.postalCode = ?", params);
    }

    @Override
    public List<Customer> customersByState(final String state) {
        if((state == null) || (state.isEmpty())) {
            throw new IllegalArgumentException("state cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = state;
        return read("from Customer customer where customer.address.state = ?", params);
    }


    @Override
    public Customer customerBySignature(final String signature) {
        if((signature == null) || (signature.isEmpty())) {
            throw new IllegalArgumentException("signature cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = signature;
        return read("from Customer customer where customer.signature = ?", params).get(0);
    }

    @Override
    public Customer customerByPin(final String pin) {
        if((pin == null) || (pin.isEmpty())) {
            throw new IllegalArgumentException("pin cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = pin;
        return read("from Customer customer where customer.pin = ?", params).get(0);
    }

    @Override
    public Customer customerByPassword(final String password) {
        if((password == null) || (password.isEmpty())) {
            throw new IllegalArgumentException("password cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = password;
        return read("from Customer customer where customer.password = ?", params).get(0);
    }

    @Override
    public Customer customerByEmail(final String email) {
        if((email == null) || (email.isEmpty())) {
            throw new IllegalArgumentException("email cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = email;
        return read("from Customer customer where customer.contact.email = ?", params).get(0);
    }

    @Override
    public Customer customerByPhoneNumber(final String phoneNumber) {
        if((phoneNumber == null) || (phoneNumber.isEmpty())) {
            throw new IllegalArgumentException("phoneNumber cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = phoneNumber;
        return read("from Customer customer where customer.contact.phoneNumber = ?", params).get(0);
    }

    @Override
    public Customer customerByAccount(final Account account) {
        if(account == null) {
            throw new IllegalArgumentException("account cannot be null!");
        }
        Object[] params = new Object[1];
        params[0] = account.getId();
        return read("select c from Customer as c left join c.accounts as a where a.id = ?", params).get(0);
    }

    @Override
    public List<Customer> fetchAll() {
        return read("from Customer", null);
    }

}

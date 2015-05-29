package pl.mefiu.bank;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class EntitiesFactory extends AbstractFactory {

    private static AbstractFactory instance = new EntitiesFactory();

    public static AbstractFactory getInstance() {
        return instance;
    }

    protected EntitiesFactory() {

    }

    @Override
    public Customer createCustomer(String firstName, String secondName, Date dateOfBirth, String signature, String pin,
                                   String city, String postalCode, String street, String apartmentNumber, String state,
                                   String email, String phoneNumber, Session session) {
        session.beginTransaction();
        Query query = session.createQuery("from Customer");
        List<Customer> results = query.list();
        String password;
        if(results.size() == 0) {
            password = RandomStringUtils.randomAlphanumeric(4);
        } else {
            final List<String> passwords = new ArrayList<>();
            results.stream().forEach(customer -> passwords.add(customer.getPassword()));
            String passwordsAsString = "";
            for(String pass : passwords) {
                passwordsAsString += pass;
                passwordsAsString += "\n";
            }
            do {
                password = RandomStringUtils.randomAlphanumeric(4);
                if(passwordsAsString.contains(password)) {
                    continue;
                } else {
                    break;
                }
            } while(true);
        }
        Customer customer = new Customer(firstName, secondName, dateOfBirth, signature, pin, password,
                                         new Address(city, postalCode, street, apartmentNumber, state),
                                         new Contact(email, phoneNumber));
        session.save(customer);
        session.getTransaction().commit();
        session.close();
        return customer;
    }

    @Override
    public Employee createOrdinaryEmployee(String name, Date expiresAt, Session session) {
        session.beginTransaction();
        Query query = session.createQuery("from Employee");
        List<Employee> results = query.list();
        String accessCode;
        if(results.size() == 0) {
            accessCode = RandomStringUtils.randomAlphanumeric(4);
        } else {
            final List<String> passwords = new ArrayList<>();
            results.stream().forEach(employee -> passwords.add(employee.getAccessCode()));
            String passwordsAsString = "";
            for(String password : passwords) {
                passwordsAsString += password;
                passwordsAsString += "\n";
            }
            do {
                accessCode = RandomStringUtils.randomAlphanumeric(4);
                if(passwordsAsString.contains(accessCode)) {
                    continue;
                } else {
                    break;
                }
            } while(true);
        }
        Employee employee = new Employee(Employee.EmployeeRole.ORDINARY, name, accessCode, expiresAt);
        session.save(employee);
        session.getTransaction().commit();
        session.close();
        return employee;
    }

    @Override
    public Employee createAdminEmployee(String name, Date expiresAt, Session session) {
        session.beginTransaction();
        Query query = session.createQuery("from Employee");
        List<Employee> results = query.list();
        String accessCode;
        if(results.size() == 0) {
            accessCode = RandomStringUtils.randomAlphanumeric(4);
        } else {
            final List<String> passwords = new ArrayList<>();
            results.stream().forEach(employee -> passwords.add(employee.getAccessCode()));
            String passwordsAsString = "";
            for(String password : passwords) {
                passwordsAsString += password;
                passwordsAsString += "\n";
            }
            do {
                accessCode = RandomStringUtils.randomAlphanumeric(4);
                if(passwordsAsString.contains(accessCode)) {
                    continue;
                } else {
                    break;
                }
            } while(true);
        }
        Employee employee = new Employee(Employee.EmployeeRole.ADMIN, name, accessCode, expiresAt);
        session.save(employee);
        session.getTransaction().commit();
        session.close();
        return employee;
    }

    @Override
    public Account createAccount(Date expiresAt, Double balance, String iban, Customer customer, Session session) {
        session.beginTransaction();
        Query query = session.createQuery("from Account");
        List<Account> results = query.list();
        String accountNumber;
        String code;
        if(results.size() == 0) {
            accountNumber = RandomStringUtils.randomAlphanumeric(10);
        } else {
            final List<String> accountNumbers = new ArrayList<>();
            results.stream().forEach(account -> accountNumbers.add(account.getAccountNumber()));
            String accountNumbersAsString = "";
            for(String accountNum : accountNumbers) {
                accountNumbersAsString += accountNum;
                accountNumbersAsString += "\n";
            }
            do {
                accountNumber = RandomStringUtils.randomAlphanumeric(10);
                if(accountNumbersAsString.equals(accountNumber)) {
                    continue;
                } else {
                    break;
                }
            } while(true);
        }
        if(results.size() == 0) {
            code = RandomStringUtils.randomAlphanumeric(5);
        } else {
            final List<String> codes = new ArrayList<>();
            results.stream().forEach(account -> codes.add(account.getCode()));
            String codesAsString = "";
            for(String tempCode : codes) {
                codesAsString += tempCode;
                codesAsString += "\n";
            }
            do {
                code = RandomStringUtils.randomAlphanumeric(5);
                if(codesAsString.equals(code)) {
                    continue;
                } else {
                    break;
                }
            } while(true);
        }
        Account account = new AccountImpl(expiresAt, balance, iban, accountNumber, code, customer);
        session.save(account);
        customer.addAccount(account);
        session.getTransaction().commit();
        session.close();
        return account;
    }

    @Override
    public Account createDebitAccount(Account account) {
        return new DebitAccountImpl(account);
    }

    @Override
    public CustomerEmployer createCustomerEmployer(Date expiresAt, Double salary, String city, String postalCode,
                                                   String street, String apartmentNumber, String state, String email,
                                                   String phoneNumber,  String name, Customer customer, Session session) {
        session.beginTransaction();
        CustomerEmployer customerEmployer = new CustomerEmployer(expiresAt, salary,  new Contact(email, phoneNumber),
                new Address(city, postalCode, street, apartmentNumber, state), name, customer);
        session.save(customerEmployer);
        customer.addCustomerEmployer(customerEmployer);
        session.getTransaction().commit();
        session.close();
        return customerEmployer;
    }

}

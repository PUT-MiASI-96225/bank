package pl.mefiu.bank;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IBank {

    void acceptPayload(Double amount, String accountNumber);

    Customer createCustomer(String firstName, String secondName, Date dateOfBirth, String signature, String pin,
                            String city, String postalCode, String street, String apartmentNumber, String state,
                            String email, String phoneNumber);

    Employee createOrdinaryEmployee(String name, Date expiresAt);

    Employee createAdminEmployee(String name, Date expiresAt);

    Account createAccount(Date expiresAt, Double balance, Customer customer);

    Account createDebitAccount(Account account);

    CustomerEmployer createCustomerEmployer(Date expiresAt, Double salary, String city,
                                            String postalCode, String street, String apartmentNumber,
                                            String state, String email, String phoneNumber, String name,
                                            Customer customer);

    Customer customerByPin(final String pin);

    Employee employeeByName(final String name);

    List<Employee> fetchAllEmployees();

    List<Customer> fetchAllCustomers();

    void updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    void deleteCustomer(Customer customer);

    void generateReport(Account account) throws COSVisitorException, ParseException, IOException;

    AccountVisitor getAccountVisitor();

    void setAccountVisitor(AccountVisitor accountVisitor);

    String getReportGeneratorPath();

    void setReportGeneratorPath(String reportGeneratorPath);

    void deleteAccount(Account account);

    void deleteCustomerEmployer(CustomerEmployer customerEmployer);

    void makeWithdraw(Account account, Double amount, boolean queue, EventHandler<WorkerStateEvent> onSuccess,
                      EventHandler<WorkerStateEvent> onFail, EventHandler<WorkerStateEvent> onProcessing);

    void makeDeposit(Account account, Double amount, boolean queue, EventHandler<WorkerStateEvent> onSuccess,
                     EventHandler<WorkerStateEvent> onFail, EventHandler<WorkerStateEvent> onProcessing);

    void makeTransferBetweenBanks(EventHandler<WorkerStateEvent> onSuccess, EventHandler<WorkerStateEvent> onFail,
                                  EventHandler<WorkerStateEvent> onProcessing, Double amount, String otherAccountNumber,
                                  String otherAccountIban, Account account, boolean queue);

    void makeTransferBetweenAccounts(EventHandler<WorkerStateEvent> onSuccess, EventHandler<WorkerStateEvent> onFail,
                                     EventHandler<WorkerStateEvent> onProcessing, Double amount,
                                     String otherAccountNumber, Account account, boolean queue);

    List<Account> fetchAllAccounts();

}

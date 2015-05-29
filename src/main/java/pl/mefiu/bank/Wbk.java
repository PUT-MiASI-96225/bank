package pl.mefiu.bank;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public final class Wbk implements IBank {

    public void generateReport(Account account) throws COSVisitorException, ParseException, IOException {
        account.acceptVisitor(getAccountVisitor());
    }

    public TransactionsProcessor getTransactionsProcessor() {
        return transactionsProcessor;
    }

    public void setTransactionsProcessor(TransactionsProcessor transactionsProcessor) {
        if(transactionsProcessor != null) {
            this.transactionsProcessor = transactionsProcessor;
        } else {
            throw new IllegalArgumentException("transactionsProcessor cannot be null!");
        }
    }

    public IBankMediator getiBankMediator() {
        return iBankMediator;
    }

    public void setiBankMediator(IBankMediator iBankMediator) {
        if(iBankMediator != null) {
            this.iBankMediator = iBankMediator;
        } else {
            throw new IllegalArgumentException("iBankMediator cannot be null!");
        }
    }

    public String getReportGeneratorPath() {
        return reportGeneratorPath;
    }

    public void setReportGeneratorPath(String reportGeneratorPath) {
        if((reportGeneratorPath != null) && (!reportGeneratorPath.isEmpty())) {
            this.reportGeneratorPath = reportGeneratorPath;
        } else {
            throw new IllegalArgumentException("reportGeneratorPath cannot be empty/null!");
        }
    }

    public AccountVisitor getAccountVisitor() {
        return accountVisitor;
    }

    public void setAccountVisitor(AccountVisitor accountVisitor) {
        if(accountVisitor != null) {
            this.accountVisitor = accountVisitor;
        } else {
            throw new IllegalArgumentException("accountVisitor cannot be null!");
        }
    }

    public AbstractFactory getEntitiesFactory() {
        return entitiesFactory;
    }

    public void setEntitiesFactory(AbstractFactory entitiesFactory) {
        if(entitiesFactory != null) {
            this.entitiesFactory = entitiesFactory;
        } else {
            throw new IllegalArgumentException("entitiesFactory cannot be null!");
        }
    }

    @Override
    public void acceptPayload(Double amount, String accountNumber) {
        IAccountRepository accountRepository = createAccountRepository();
        Account account = accountRepository.accountByAccountNumber(accountNumber);
        if(account == null) {
            throw new IllegalStateException("there is no such account!");
        }
        account.makeDeposit(amount, false, getSession(), getTransactionsProcessor());
    }

    @Override
    public Customer createCustomer(String firstName, String secondName, Date dateOfBirth, String signature, String pin, String city, String postalCode, String street, String apartmentNumber, String state, String email, String phoneNumber) {
        return getEntitiesFactory().createCustomer(firstName, secondName, dateOfBirth, signature, pin, city, postalCode, street, apartmentNumber, state, email, phoneNumber, getSession());
    }

    @Override
    public Employee createOrdinaryEmployee(String name, Date expiresAt) {
        return getEntitiesFactory().createOrdinaryEmployee(name, expiresAt, getSession());
    }

    @Override
    public Employee createAdminEmployee(String name, Date expiresAt) {
        return getEntitiesFactory().createAdminEmployee(name, expiresAt, getSession());
    }

    @Override
    public Account createAccount(Date expiresAt, Double balance, Customer customer) {
        return getEntitiesFactory().createAccount(expiresAt, balance, bankIBAN, customer, getSession());
    }

    @Override
    public Account createDebitAccount(Account account) {
        return getEntitiesFactory().createDebitAccount(account);
    }

    @Override
    public CustomerEmployer createCustomerEmployer(Date expiresAt, Double salary, String city, String postalCode, String street, String apartmentNumber, String state, String email, String phoneNumber, String name, Customer customer) {
        return getEntitiesFactory().createCustomerEmployer(expiresAt, salary, city, postalCode, street, apartmentNumber, state, email, phoneNumber, name, customer, getSession());
    }

    public Wbk(IBankMediator iBankMediator) {
        setiBankMediator(iBankMediator);
        bankIBAN = getiBankMediator().registerBank(this);
        setTransactionsProcessor(new TransactionsProcessor());
        setReportGeneratorPath("wbk.pdf");
        setAccountVisitor(new AccountFullReportGenerator(getReportGeneratorPath()));
        setEntitiesFactory(new EntitiesFactory());
    }

    @Override
    public Customer customerByPin(final String pin) {
        return createCustomerRepository().customerByPin(pin);
    }

    @Override
    public Employee employeeByName(final String name) {
        return createEmployeeRepository().employeeByName(name);
    }

    @Override
    public List<Employee> fetchAllEmployees() {
        return createEmployeeRepository().fetchAll();
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        return createCustomerRepository().fetchAll();
    }

    @Override
    public List<Account> fetchAllAccounts() {
        return createAccountRepository().fetchAll();
    }

    @Override
    public void makeTransferBetweenBanks(EventHandler<WorkerStateEvent> onSuccess, EventHandler<WorkerStateEvent> onFail,
                                         EventHandler<WorkerStateEvent> onProcessing, Double amount, String otherAccountNumber,
                                         String otherAccountIban, Account account, boolean queue) {
        TransactionsProcessor transactionsProcessor = getTransactionsProcessor();
        transactionsProcessor.setOnSucceeded(onSuccess);
        transactionsProcessor.setOnFailed(onFail);
        transactionsProcessor.setOnRunning(onProcessing);
        account.makeTransferBetweenBanks(amount, otherAccountIban, otherAccountNumber, queue, getSession(),
                getiBankMediator(), transactionsProcessor);
    }

    @Override
    public void makeTransferBetweenAccounts(EventHandler<WorkerStateEvent> onSuccess, EventHandler<WorkerStateEvent> onFail,
                                            EventHandler<WorkerStateEvent> onProcessing, Double amount,
                                            String otherAccountNumber, Account account, boolean queue) {
        TransactionsProcessor transactionsProcessor = getTransactionsProcessor();
        transactionsProcessor.setOnSucceeded(onSuccess);
        transactionsProcessor.setOnFailed(onFail);
        transactionsProcessor.setOnRunning(onProcessing);
        Account otherAccount = createAccountRepository().accountByAccountNumber(otherAccountNumber);
        System.out.println("OK!");
        account.makeTransferBetweenAccounts(amount, otherAccount, queue, getSession(), transactionsProcessor);
    }

    @Override
    public void updateEmployee(Employee employee) {
        ((Repository)createEmployeeRepository()).update(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        ((Repository)createEmployeeRepository()).delete(employee);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        ((Repository)createCustomerRepository()).delete(customer);
    }

    @Override
    public void deleteAccount(Account account) {
        ((Repository) createAccountRepository()).delete(account);
    }

    @Override
    public void deleteCustomerEmployer(CustomerEmployer customerEmployer) {
        ((Repository) createCustomerEmployerRepository()).delete(customerEmployer);
    }

    private ICustomerEmployerRepository createCustomerEmployerRepository() {
        Repository customerEmployerRepository = new CustomerEmployerRepository();
        customerEmployerRepository.setRepositoryImpl(new HibernateRepository(getSession()));
        return (ICustomerEmployerRepository) customerEmployerRepository;
    }

    private IAccountRepository createAccountRepository() {
        Repository accountRepository = new AccountRepository();
        accountRepository.setRepositoryImpl(new HibernateRepository(getSession()));
        return (IAccountRepository) accountRepository;
    }

    private ICustomerRepository createCustomerRepository() {
        Repository customerRepository = new CustomerRepository();
        customerRepository.setRepositoryImpl(new HibernateRepository(getSession()));
        return (ICustomerRepository) customerRepository;
    }

    private IEmployeeRepository createEmployeeRepository() {
        Repository employeeRepository = new EmployeeRepository();
        employeeRepository.setRepositoryImpl(new HibernateRepository(getSession()));
        return (IEmployeeRepository) employeeRepository;
    }

    @Override
    public void makeWithdraw(Account account, Double amount, boolean queue, EventHandler<WorkerStateEvent> onSuccess,
                             EventHandler<WorkerStateEvent> onFail, EventHandler<WorkerStateEvent> onProcessing) {
        TransactionsProcessor transactionsProcessor = getTransactionsProcessor();
        transactionsProcessor.setOnSucceeded(onSuccess);
        transactionsProcessor.setOnFailed(onFail);
        transactionsProcessor.setOnRunning(onProcessing);
        account.makeWithdraw(amount, queue, getSession(), transactionsProcessor);
    }

    @Override
    public void makeDeposit(Account account, Double amount, boolean queue, EventHandler<WorkerStateEvent> onSuccess,
                            EventHandler<WorkerStateEvent> onFail, EventHandler<WorkerStateEvent> onProcessing) {
        account.makeDeposit(amount, queue, getSession(), getTransactionsProcessor());
    }

    private Session getSession() {
        return HibernateUtil.getWbkSession();
    }

    private IBankMediator iBankMediator;

    private TransactionsProcessor transactionsProcessor;

    private String reportGeneratorPath;

    private AccountVisitor accountVisitor;

    private AbstractFactory entitiesFactory;

    private String bankIBAN;

}

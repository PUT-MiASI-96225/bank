package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;
import org.junit.Test;
import pl.mefiu.bank.AccountPeriodReportGenerator.AccountPeriodReportGeneratorContext;
import pl.mefiu.bank.TransferBetweenAccountsTransaction.TransferBetweenAccountsTransactionContext;
import pl.mefiu.bank.TransferBetweenBanksTransaction.TransferBetweenBanksTransactionContext;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class AppTest {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final String PKO_DB_URL = "jdbc:mysql://localhost:3306/pko";

    private static final String WBK_DB_URL = "jdbc:mysql://localhost:3306/wbk";

    private static final String USER = "matt";

    private static final String PASS = "matt";

    private static final Double BALANCE = 1000.0;

    private static final Double AMOUNT = 100.0;

    private static final Double SALARY = 2000.0;

    // Check creation of admin employees

    @Test
    public void testAdminFactory() {
        ElixirBankMediator elixirBankMediator = new ElixirBankMediator();
        Pko pko = new Pko(elixirBankMediator);
        Wbk wbk = new Wbk(elixirBankMediator);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 2);
        Date newDate = calendar.getTime();

        final String empName = "matt";

        pko.createAdminEmployee(empName, newDate);

        wbk.createAdminEmployee(empName, newDate);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(PKO_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select name from employee where name = ?");
            preparedStatement.setString(1, empName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                assertTrue(name.equals(empName));
            }
            connection = DriverManager.getConnection(WBK_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select name from employee where name = ?");
            preparedStatement.setString(1, empName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                assertTrue(name.equals(empName));
            } else {
                assertTrue(false);
            }
            resultSet.close();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(preparedStatement != null) {
                    connection.close();
                }
            } catch(SQLException se) {
            }
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Check creation of ordinary employees

    @Test
    public void testEmployeeFactory() {
        ElixirBankMediator elixirBankMediator = new ElixirBankMediator();
        Pko pko = new Pko(elixirBankMediator);
        Wbk wbk = new Wbk(elixirBankMediator);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 2);
        Date newDate = calendar.getTime();

        final String empName = "mefiu";

        pko.createOrdinaryEmployee(empName, newDate);

        wbk.createOrdinaryEmployee(empName, newDate);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(PKO_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select name from employee where name = ?");
            preparedStatement.setString(1, empName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                assertTrue(name.equals(empName));
            }
            connection = DriverManager.getConnection(WBK_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select name from employee where name = ?");
            preparedStatement.setString(1, empName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                assertTrue(name.equals(empName));
            } else {
                assertTrue(false);
            }
            resultSet.close();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(preparedStatement != null) {
                    connection.close();
                }
            } catch(SQLException se) {
            }
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Test customer, customer employers and account factories

    @Test
    public void testCustomerFactory() {
        ElixirBankMediator elixirBankMediator = new ElixirBankMediator();
        Pko pko = new Pko(elixirBankMediator);
        Wbk wbk = new Wbk(elixirBankMediator);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -20);
        Date oldDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 22);
        Date newDate = calendar.getTime();

        final String firstName = "mefiu";

        final String secondName = "mefiu";

        final String name = firstName + " " + secondName;

        final String signature = "mefiu";

        final String pin = "x1x1x";

        final String city = "/dev/null";

        final String postalCode = "66-666";

        final String street = "xxx";

        final String apartmentNumber = "12";

        final String state = "wlkp";

        final String email = "a@a.pl";

        final String phoneNumber = "12345";

        Customer pkoCustomer = pko.createCustomer(firstName, secondName, oldDate, signature, pin, city, postalCode,
                street, apartmentNumber, state, email, phoneNumber);

        Customer wbkCustomer = wbk.createCustomer(firstName, secondName, oldDate, signature, pin, city, postalCode,
                street, apartmentNumber, state, email, phoneNumber);

        final Double salary = SALARY;

        pko.createCustomerEmployer(newDate, salary, city, postalCode, street, apartmentNumber, state, email, phoneNumber,
                name, pkoCustomer);

        wbk.createCustomerEmployer(newDate, salary, city, postalCode, street, apartmentNumber, state, email, phoneNumber,
                name, wbkCustomer);

        final Double balance = BALANCE;

        pko.createAccount(newDate, balance, pkoCustomer);

        wbk.createAccount(newDate, balance, wbkCustomer);

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(PKO_DB_URL, USER, PASS);
            statement = connection.createStatement();
            String sql = "select email from account a inner join customer c on c.id = a.id";
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                String mail = resultSet.getString("email");
                assertTrue(mail.equals(email));
            } else {
                assertTrue(false);
            }
            connection = DriverManager.getConnection(WBK_DB_URL, USER, PASS);
            statement = connection.createStatement();
            sql = "select email from account a inner join customer c on c.id = a.id";
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                String mail = resultSet.getString("email");
                assertTrue(mail.equals(email));
            } else {
                assertTrue(false);
            }
            resultSet.close();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(statement != null) {
                    connection.close();
                }
            } catch(SQLException se) {
            }
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Check transactions: debit, deposit, withdraw, transfer between accounts, transfer between banks

    @Test
    public void transactionsTest() throws COSVisitorException, ParseException, IOException, ClassNotFoundException, SQLException {
        ElixirBankMediator elixirBankMediator = new ElixirBankMediator();
        Pko pko = new Pko(elixirBankMediator);
        Wbk wbk = new Wbk(elixirBankMediator);

        File wbkFile = new File("wbk.pdf");
        File pkoFile = new File("pko.pdf");

        File logFile = new File("logfile.log");

        if((wbkFile.exists()) && (wbkFile.isFile())) {
            wbkFile.delete();
        }

        if((pkoFile.exists()) && (pkoFile.isFile())) {
            pkoFile.delete();
        }

        if((logFile.exists()) && (logFile.isFile())) {
            logFile.delete();
        }

        List<Account> pkoAccounts = pko.fetchAllAccounts();
        List<Account> wbkAccounts = wbk.fetchAllAccounts();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        if((pkoAccounts.size() != 1) && (wbkAccounts.size() != 1)) {
            assertTrue(false);
        } else {
            Class.forName(JDBC_DRIVER);
            Account pkoAccount = pkoAccounts.get(0);
            Account wbkAccount = wbkAccounts.get(0);
            Session pkoSession;
            connection = DriverManager.getConnection(PKO_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select balance from account where id = ?");
            preparedStatement.setLong(1, pkoAccount.getId());
            ResultSet resultSet;
            Transaction depositTransaction;
            pkoSession = HibernateUtil.getPkoSession();
            depositTransaction = new DepositTransaction(AMOUNT, pkoAccount, pkoSession);
            depositTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE + AMOUNT);
            Transaction withdrawTransaction;
            pkoSession = HibernateUtil.getPkoSession();
            withdrawTransaction = new WithdrawTransaction(AMOUNT, pkoAccount, pkoSession);
            withdrawTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            pkoSession = HibernateUtil.getPkoSession();
            withdrawTransaction = new WithdrawTransaction(BALANCE, pkoAccount, pkoSession);
            withdrawTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == 0.0);
            pkoSession = HibernateUtil.getPkoSession();
            WithdrawWithDebitTransaction withdrawWithDebitTransaction = new WithdrawWithDebitTransaction(AMOUNT, pkoAccount, pkoSession);
            withdrawWithDebitTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == -AMOUNT);
            pkoSession = HibernateUtil.getPkoSession();
            depositTransaction = new DepositTransaction(AMOUNT + BALANCE, pkoAccount, pkoSession);
            depositTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            pkoSession = HibernateUtil.getPkoSession();
            TransferBetweenAccountsTransactionContext actx = new TransferBetweenAccountsTransactionContext(AMOUNT,
                    pkoAccount, pkoAccount, pkoSession);
            TransferBetweenAccountsTransaction transferBetweenAccountsTransaction = new TransferBetweenAccountsTransaction(actx);
            transferBetweenAccountsTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            Session wbkSession;
            connection = DriverManager.getConnection(WBK_DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("select balance from account where id = ?");
            preparedStatement.setLong(1, wbkAccount.getId());
            wbkSession = HibernateUtil.getWbkSession();
            depositTransaction = new DepositTransaction(AMOUNT, wbkAccount, wbkSession);
            depositTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE + AMOUNT);
            wbkSession = HibernateUtil.getWbkSession();
            withdrawTransaction = new WithdrawTransaction(AMOUNT, wbkAccount, wbkSession);
            withdrawTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            wbkSession = HibernateUtil.getWbkSession();
            withdrawTransaction = new WithdrawTransaction(BALANCE, wbkAccount, wbkSession);
            withdrawTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == 0.0);
            wbkSession = HibernateUtil.getWbkSession();
            withdrawWithDebitTransaction = new WithdrawWithDebitTransaction(AMOUNT, wbkAccount, wbkSession);
            withdrawWithDebitTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == -AMOUNT);
            wbkSession = HibernateUtil.getWbkSession();
            depositTransaction = new DepositTransaction(AMOUNT + BALANCE, wbkAccount, wbkSession);
            depositTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            wbkSession = HibernateUtil.getWbkSession();
            actx = new TransferBetweenAccountsTransactionContext(AMOUNT,
                    wbkAccount, wbkAccount, wbkSession);
            transferBetweenAccountsTransaction = new TransferBetweenAccountsTransaction(actx);
            transferBetweenAccountsTransaction.execute();
            resultSet = preparedStatement.executeQuery();
            assertTrue(resultSet.next());
            assertTrue(resultSet.getDouble("balance") == BALANCE);
            TransferBetweenBanksTransactionContext bctx = new TransferBetweenBanksTransactionContext(BALANCE,
                    wbkAccount.getIban(), wbkAccount.getAccountNumber(), pkoAccount, pkoSession, elixirBankMediator);
            TransferBetweenBanksTransaction transferBetweenBanksTransaction = new TransferBetweenBanksTransaction(bctx);
            try {
                transferBetweenBanksTransaction.execute();
            } catch(Exception e) {
            }
            AccountFullReportGenerator accountFullReportGenerator;
            accountFullReportGenerator = new AccountFullReportGenerator(pko.getReportGeneratorPath());
            AccountDepositReportGenerator accountDepositReportGenerator;
            accountDepositReportGenerator = new AccountDepositReportGenerator(pko.getReportGeneratorPath());
            AccountWithdrawReportGenerator accountWithdrawReportGenerator;
            accountWithdrawReportGenerator = new AccountWithdrawReportGenerator(pko.getReportGeneratorPath());
            AccountPeriodReportGeneratorContext accountPeriodReportGeneratorContext;
            accountPeriodReportGeneratorContext = new AccountPeriodReportGeneratorContext(new Date(), new Date(), pko.getReportGeneratorPath());
            AccountPeriodReportGenerator accountPeriodReportGenerator;
            accountPeriodReportGenerator = new AccountPeriodReportGenerator(accountPeriodReportGeneratorContext);
            pkoAccount.acceptVisitor(accountDepositReportGenerator);
            pkoAccount.acceptVisitor(accountFullReportGenerator);
            pkoAccount.acceptVisitor(accountWithdrawReportGenerator);
            pkoAccount.acceptVisitor(accountPeriodReportGenerator);
            accountFullReportGenerator = new AccountFullReportGenerator(wbk.getReportGeneratorPath());
            accountDepositReportGenerator = new AccountDepositReportGenerator(wbk.getReportGeneratorPath());
            accountWithdrawReportGenerator = new AccountWithdrawReportGenerator(wbk.getReportGeneratorPath());
            accountPeriodReportGeneratorContext = new AccountPeriodReportGeneratorContext(new Date(), new Date(), wbk.getReportGeneratorPath());
            accountPeriodReportGenerator = new AccountPeriodReportGenerator(accountPeriodReportGeneratorContext);
            wbkAccount.acceptVisitor(accountDepositReportGenerator);
            wbkAccount.acceptVisitor(accountFullReportGenerator);
            wbkAccount.acceptVisitor(accountWithdrawReportGenerator);
            wbkAccount.acceptVisitor(accountPeriodReportGenerator);
            assertTrue(pkoFile.exists());
            assertTrue(wbkFile.exists());
        }
    }

}

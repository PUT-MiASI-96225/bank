package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;
import org.hibernate.annotations.ForceDiscriminator;

import javax.persistence.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("ALL")
@Entity
@Inheritance
@DiscriminatorColumn(name = "Acc")
@ForceDiscriminator
@Table(name = "account")
public abstract class Account {

    public abstract void makeDeposit(Double amount, boolean queueTransaction, Session session,
                                     TransactionsProcessor transactionsProcessor);

    public abstract void makeTransferBetweenAccounts(Double amount, Account otherAccount, boolean queueTransaction,
                                                     Session session, TransactionsProcessor transactionsProcessor);

    public abstract void makeTransferBetweenBanks(Double amount, String otherAccountIban, String otherAccountNumber,
                                                  boolean queueTransaction, Session session, IBankMediator iBankMediator,
                                                  TransactionsProcessor transactionsProcessor);

    public abstract void makeWithdraw(Double amount, boolean queueTransaction, Session session,
                                      TransactionsProcessor transactionsProcessor);

    public abstract void acceptVisitor(AccountVisitor accountVisitor) throws IOException, COSVisitorException,
            ParseException;

    public Account(Date expiresAt, Double balance, String iban, String accountNumber, String code, Customer customer) {
        setCreatedAt(new Date());
        setExpiresAt(expiresAt);
        if(balance != null) {
            if(balance < 0.0) {
                throw new IllegalArgumentException("balance cannot be less than 0 at creation time!");
            }
        }
        setBalance(balance);
        setIban(iban);
        setAccountNumber(accountNumber);
        setCode(code);
        setTransactions(new HashSet<>());
        setCustomer(customer);
    }

    protected Account() {

    }

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public Date getExpiresAt() {
        return new Date(expiresAt.getTime());
    }

    public Double getBalance() {
        return balance;
    }

    public String getIban() {
        return iban;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCode() {
        return code;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(Date expiresAt) {
        if(expiresAt != null) {
            if(expiresAt.before(getCreatedAt())) {
                throw new IllegalArgumentException("bad date!");
            }
        }
        this.expiresAt = expiresAt;
    }

    protected void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setIban(String iban) {
        if((iban != null) && (iban.isEmpty())) {
            throw new IllegalArgumentException("iban cannot be empty!");
        }
        this.iban = iban;
    }

    public void setAccountNumber(String accountNumber) {
        if((accountNumber != null) && (accountNumber.isEmpty())) {
            throw new IllegalArgumentException("accountNumber cannot be empty!");
        }
        this.accountNumber = accountNumber;
    }

    public void setCode(String code) {
        if((code != null) && (code.isEmpty())) {
            throw new IllegalArgumentException("code cannot be empty!");
        }
        this.code = code;
    }

    private void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    protected void addTransaction(Transaction transaction) {
        if(transaction == null) {
            throw new IllegalArgumentException("transaction cannot be null!");
        }
        transactions.add(transaction);
    }

    protected void resetPendingTransactions() {
        pendingTransactions.clear();
    }

    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at", nullable = false, unique = false)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "expires_at", nullable = false, unique = false)
    private Date expiresAt;

    @Column(name = "balance", nullable = false, unique = false)
    private Double balance;

    @Column(name = "iban", nullable = false, unique = false)
    private String iban;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions;

    @ManyToOne
    private Customer customer;

    @Transient
    protected List<Transaction> pendingTransactions = new ArrayList<>();

}

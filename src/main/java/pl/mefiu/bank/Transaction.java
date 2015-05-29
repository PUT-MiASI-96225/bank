package pl.mefiu.bank;

import org.hibernate.annotations.ForceDiscriminator;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("ALL")
@Entity
@Inheritance
@DiscriminatorColumn(name = "TR")
@ForceDiscriminator
@Table(name = "transaction")
public abstract class Transaction {

    public enum TransactionState {
        FAILED, SUCCEED
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER_BETWEEN_ACCOUNTS, TRANSFER_BETWEEN_BANKS
    }

    public void execute() {
        prepareTransaction();
        finishTransaction(makeTransaction());
    }

    protected Transaction() {

    }

    protected abstract void prepareTransaction();

    protected abstract boolean makeTransaction();

    protected abstract void finishTransaction(boolean succeed);

    public Long getId() {
        return id;
    }

    public TransactionState getTransactionStatus() {
        if(transactionStatus == TransactionState.FAILED) {
            return TransactionState.FAILED;
        }
        return TransactionState.SUCCEED;
    }

    public TransactionType getTransactionType() {
        if(transactionType == TransactionType.DEPOSIT) {
            return TransactionType.DEPOSIT;
        } else if(transactionType == TransactionType.WITHDRAW) {
            return  TransactionType.WITHDRAW;
        } else if(transactionType == TransactionType.TRANSFER_BETWEEN_ACCOUNTS) {
            return TransactionType.TRANSFER_BETWEEN_ACCOUNTS;
        }
        return TransactionType.TRANSFER_BETWEEN_BANKS;
    }

    public Account getAccount() {
        return account;
    }

    public Date getStartedAt() {
        return new Date(startedAt.getTime());
    }

    public Date getCompletedAt() {
        return new Date(completedAt.getTime());
    }

    public String getDescription() {
        return description;
    }

    public Transaction getNextTransaction() {
        return nextTransaction;
    }

    protected void setTransactionStatus(TransactionState transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    protected void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    protected void setAccount(Account account) {
        if(account == null) {
            throw new IllegalArgumentException("account cannot be null!");
        }
        this.account = account;
    }

    protected void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    protected void setCompletedAt(Date completedAt) throws IllegalArgumentException {
        if(completedAt != null) {
            if(completedAt.before(getStartedAt())) {
                throw new IllegalArgumentException("bad date!");
            }
        }
        this.completedAt = completedAt;
    }

    protected void setDescription(String description) {
        if((description != null) && (description.isEmpty())) {
            throw new IllegalArgumentException("description cannot be empty!");
        }
        this.description = description;
    }

    public void setNextTransaction(Transaction nextTransaction) {
        this.nextTransaction = nextTransaction;
    }

    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "transaction_state", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private TransactionState transactionStatus;

    @Column(name = "transaction_type", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    private Account account;

    @Temporal(TemporalType.DATE)
    @Column(name = "started_at", nullable = false, unique = false)
    private Date startedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "completed_at", nullable = false, unique = false)
    private Date completedAt;

    @Column(name = "description", nullable = false, unique = false)
    private String description;

    @Transient
    private Transaction nextTransaction;

}

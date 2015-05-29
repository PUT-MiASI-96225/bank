package pl.mefiu.bank;

import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("DTR")
public final class DepositTransaction extends Transaction {

    public DepositTransaction(Double amount, Account account, Session session) {
        if((amount != null) && (amount != 0)) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("amount cannot be null/zero!");
        }
        setAccount(account);
        if(session != null) {
            this.session = session;
        } else {
            throw new IllegalArgumentException("session cannot be null!");
        }
    }

    protected DepositTransaction() {

    }

    @Override
    protected void prepareTransaction() {
        setTransactionType(TransactionType.DEPOSIT);
        setStartedAt(new Date());
    }

    @Override
    protected boolean makeTransaction() {
        Account account = getAccount();
        Double balance = account.getBalance();
        balance += amount;
        account.setBalance(balance);
        return true;
    }

    @Override
    protected void finishTransaction(boolean succeed) {
        if(succeed) {
            setTransactionStatus(TransactionState.SUCCEED);
        } else {
            setTransactionStatus(TransactionState.FAILED);
        }
        setCompletedAt(new Date());
        setDescription("[DEPOSIT] => " + amount);
        Transaction transaction = getNextTransaction();
        session.getTransaction().begin();
        session.save(this);
        getAccount().addTransaction(this);
        session.update(getAccount());
        session.getTransaction().commit();
        if(transaction != null) {
            transaction.execute();
        } else {
            session.close();
        }
    }

    @Transient
    private Double amount;

    @Transient
    private Session session;

}

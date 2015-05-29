package pl.mefiu.bank;

import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("WTR")
public final class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(Double amount, Account account, Session session) {
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

    protected WithdrawTransaction() {

    }

    @Override
    protected void prepareTransaction() {
        setTransactionType(TransactionType.WITHDRAW);
        setStartedAt(new Date());
    }

    @Override
    protected boolean makeTransaction() {
        Account account = getAccount();
        if(account.getBalance() < amount) {
            return false;
        } else {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
    }

    @Override
    protected void finishTransaction(boolean succeed) {
        if(succeed) {
            setTransactionStatus(TransactionState.SUCCEED);
        } else {
            setTransactionStatus(TransactionState.FAILED);
        }
        setCompletedAt(new Date());
        setDescription("[WITHDRAW] => " + amount);
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

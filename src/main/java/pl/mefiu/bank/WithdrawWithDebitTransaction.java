package pl.mefiu.bank;

import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("WDTR")
public class WithdrawWithDebitTransaction extends Transaction {

    public WithdrawWithDebitTransaction(Double amount, Account account, Session session) {
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

    protected WithdrawWithDebitTransaction() {

    }

    @Override
    protected void prepareTransaction() {
        setTransactionType(TransactionType.WITHDRAW);
        setStartedAt(new Date());
    }

    @Override
    protected boolean makeTransaction() {
        Account account = getAccount();
        Double balance = account.getBalance();
        Double res = balance - amount;
        if(res < 0.0) {
            account.setBalance(res);
            return true;
        } else {
            return false;
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
        setDescription("[WITHDRAW_WITH_DEBIT] => " + amount);
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

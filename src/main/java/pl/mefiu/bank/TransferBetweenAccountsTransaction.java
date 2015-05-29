package pl.mefiu.bank;

import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("TBATR")
public final class TransferBetweenAccountsTransaction extends Transaction {

    public static final class TransferBetweenAccountsTransactionContext {

        public TransferBetweenAccountsTransactionContext(Double amount, Account otherAccount, Account account, Session session) {
            if((amount != null) && (amount != 0)) {
                this.amount = amount;
            } else {
                throw new IllegalArgumentException("amount cannot be null/zero!");
            }
            if(otherAccount != null) {
                this.otherAccount = otherAccount;
            } else {
                throw new IllegalArgumentException("other account cannot be null!");
            }
            if(account != null) {
                this.account = account;
            } else {
                throw new IllegalArgumentException("account cannot be null!");
            }
            if(session != null) {
                this.session = session;
            } else {
                throw new IllegalArgumentException("session cannot be null!");
            }
        }

        private Double amount;

        private Account otherAccount;

        private Account account;

        private Session session;

    }

    public TransferBetweenAccountsTransaction(TransferBetweenAccountsTransactionContext ctx) {
        if(ctx != null) {
            this.ctx = ctx;
        } else {
            throw new IllegalArgumentException("operation context cannot be null!");
        }
        setAccount(ctx.account);
    }

    protected TransferBetweenAccountsTransaction() {

    }

    @Override
    protected void prepareTransaction() {
        setTransactionType(TransactionType.TRANSFER_BETWEEN_ACCOUNTS);
        setStartedAt(new Date());
    }

    @Override
    protected boolean makeTransaction() {
        if(ctx.account.getBalance() < ctx.amount) {
            return false;
        }
        ctx.otherAccount.setBalance(ctx.otherAccount.getBalance() + ctx.amount);
        ctx.account.setBalance(ctx.account.getBalance() - ctx.amount);
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
        setDescription("[TRANSFER_BETWEEN_ACCOUNTS] => " + ctx.amount + ", TO: " + ctx.otherAccount.getIban() + "/" + ctx.otherAccount.getAccountNumber());
        Transaction transaction = getNextTransaction();
        ctx.session.getTransaction().begin();
        ctx.session.save(this);
        getAccount().addTransaction(this);
        ctx.session.update(getAccount());
        ctx.session.update(ctx.otherAccount);
        ctx.session.getTransaction().commit();
        if(transaction != null) {
            transaction.execute();
        } else {
            ctx.session.close();
        }
    }

    @Transient
    private TransferBetweenAccountsTransactionContext ctx;

}

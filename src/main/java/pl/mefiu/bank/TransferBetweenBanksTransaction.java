package pl.mefiu.bank;

import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("TBBTR")
public final class TransferBetweenBanksTransaction extends Transaction {

    public static final class TransferBetweenBanksTransactionContext {

        public TransferBetweenBanksTransactionContext(Double amount, String otherAccountIban, String otherAccountNumber,
                                                      Account account, Session session, IBankMediator iBankMediator) {
            if((amount != null) && (amount != 0)) {
                this.amount = amount;
            } else {
                throw new IllegalArgumentException("amount cannot be null/zero!");
            }
            if((otherAccountIban != null) && (!otherAccountIban.isEmpty())) {
                this.otherAccountIban = otherAccountIban;
            } else {
                throw new IllegalArgumentException("other account iban cannot be empty/null!");
            }
            if((otherAccountNumber != null) && (!otherAccountNumber.isEmpty())) {
                this.otherAccountNumber = otherAccountNumber;
            } else {
                throw new IllegalArgumentException("other account number cannot be empty/null!");
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
            if(iBankMediator != null) {
                this.iBankMediator = iBankMediator;
            } else {
                throw new IllegalArgumentException("iBankMediator cannot be null!");
            }
        }

        private Double amount;

        private String otherAccountIban;

        private String otherAccountNumber;

        private Account account;

        private Session session;

        private IBankMediator iBankMediator;

    }

    public TransferBetweenBanksTransaction(TransferBetweenBanksTransactionContext ctx) {
        if(ctx != null) {
            this.ctx = ctx;
        } else {
            throw new IllegalArgumentException("operation context cannot be null!");
        }
        setAccount(ctx.account);
    }

    protected TransferBetweenBanksTransaction() {

    }

    @Override
    protected void prepareTransaction() {
        setTransactionType(TransactionType.TRANSFER_BETWEEN_BANKS);
        setStartedAt(new Date());
    }

    @Override
    protected boolean makeTransaction() {
        if(ctx.account.getBalance() < ctx.amount) {
            return false;
        }
        ctx.iBankMediator.transfer(ctx.otherAccountIban, ctx.otherAccountNumber, ctx.amount);
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
        setDescription("[TRANSFER_BETWEEN_BANKS] => " + ctx.amount + ", TO: " + ctx.otherAccountIban + "/" + ctx.otherAccountNumber);
        Transaction transaction = getNextTransaction();
        ctx.session.getTransaction().begin();
        ctx.session.save(this);
        getAccount().addTransaction(this);
        ctx.session.update(getAccount());
        ctx.session.getTransaction().commit();
        if(transaction != null) {
            transaction.execute();
        } else {
            ctx.session.close();
        }
    }

    @Transient
    private TransferBetweenBanksTransactionContext ctx;

}

package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;
import java.text.ParseException;

@Entity
@DiscriminatorValue("AccDec")
public abstract class AccountDecorator extends Account {

    protected AccountDecorator(Account account) {
        setAccount(account);
    }

    protected AccountDecorator() {

    }

    @Override
    public void makeDeposit(Double amount, boolean queueTransaction, Session session,
                            TransactionsProcessor transactionsProcessor) {
        getAccount().makeDeposit(amount, queueTransaction, session, transactionsProcessor);
    }

    @Override
    public void makeTransferBetweenAccounts(Double amount, Account otherAccount, boolean queueTransaction,
                                            Session session, TransactionsProcessor transactionsProcessor) {
        getAccount().makeTransferBetweenAccounts(amount, otherAccount, queueTransaction, session,
                transactionsProcessor);
    }

    @Override
    public void makeTransferBetweenBanks(Double amount, String otherAccountIban, String otherAccountNumber,
                                         boolean queueTransaction, Session session, IBankMediator iBankMediator,
                                         TransactionsProcessor transactionsProcessor) {
        getAccount().makeTransferBetweenBanks(amount, otherAccountIban, otherAccountNumber, queueTransaction,
                session, iBankMediator, transactionsProcessor);
    }

    @Override
    public void makeWithdraw(Double amount, boolean queueTransaction, Session session,
                             TransactionsProcessor transactionsProcessor) {
        getAccount().makeWithdraw(amount, queueTransaction, session, transactionsProcessor);
    }

    @Override
    public void acceptVisitor(AccountVisitor accountVisitor) throws IOException, COSVisitorException, ParseException {
        getAccount().acceptVisitor(accountVisitor);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        if(account != null) {
            this.account = account;
        } else {
            throw new IllegalArgumentException("decorated account cannot be null!");
        }
    }

    @Transient
    private Account account;

}

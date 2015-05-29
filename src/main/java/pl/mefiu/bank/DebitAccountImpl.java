package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;
import java.text.ParseException;

@Entity
@DiscriminatorValue("DebAccImp")
public final class DebitAccountImpl extends AccountDecorator {

    public DebitAccountImpl(Account account) throws IllegalArgumentException {
        super(account);
    }

    protected DebitAccountImpl() {

    }

    @Override
    public void makeDeposit(Double amount, boolean queueTransaction, Session session,
                            TransactionsProcessor transactionsProcessor) {
        super.makeDeposit(amount, queueTransaction, session, transactionsProcessor);
    }

    @Override
    public void makeTransferBetweenAccounts(Double amount, Account otherAccount, boolean queueTransaction,
                                            Session session, TransactionsProcessor transactionsProcessor) {
        super.makeTransferBetweenAccounts(amount, otherAccount, queueTransaction, session, transactionsProcessor);
    }

    @Override
    public void makeTransferBetweenBanks(Double amount, String otherAccountIban, String otherAccountNumber,
                                         boolean queueTransaction, Session session, IBankMediator iBankMediator,
                                         TransactionsProcessor transactionsProcessor) {
        super.makeTransferBetweenBanks(amount, otherAccountIban, otherAccountNumber, queueTransaction, session,
                iBankMediator, transactionsProcessor);
    }

    @Override
    public void makeWithdraw(Double amount, boolean queueTransaction, Session session,
                             TransactionsProcessor transactionsProcessor) {
        Account account = getAccount();
        if(!transactionsProcessor.isRunning()) {
            transactionsProcessor.reset();
            Transaction transaction = new WithdrawWithDebitTransaction(amount, account, session);
            if(queueTransaction == false) {
                if(pendingTransactions.isEmpty()) {
                    transactionsProcessor.setTransaction(transaction);
                    transactionsProcessor.start();
                } else {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                    transactionsProcessor.setTransaction(pendingTransactions.get(0));
                    transactionsProcessor.start();
                    pendingTransactions.clear();
                }
            } else {
                if(!pendingTransactions.isEmpty()) {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                }
                pendingTransactions.add(transaction);
            }
        } else {
            throw new IllegalStateException("transactionsProcessor is running!");
        }
    }

    @Override
    public void acceptVisitor(AccountVisitor accountVisitor) throws IOException, COSVisitorException, ParseException {
        super.acceptVisitor(accountVisitor);
    }

}

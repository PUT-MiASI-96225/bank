package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.hibernate.Session;
import pl.mefiu.bank.TransferBetweenAccountsTransaction.TransferBetweenAccountsTransactionContext;
import pl.mefiu.bank.TransferBetweenBanksTransaction.TransferBetweenBanksTransactionContext;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Entity
@DiscriminatorValue("AccImp")
public final class AccountImpl extends Account {

    public AccountImpl(Date expiresAt, Double balance, String iban, String accountNumber, String code,
                       Customer customer) {
        super(expiresAt, balance, iban, accountNumber, code, customer);
    }

    protected AccountImpl() {

    }

    @Override
    public void makeDeposit(Double amount, boolean queueTransaction, Session session,
                            TransactionsProcessor transactionsProcessor) {
        if(!transactionsProcessor.isRunning()) {
            transactionsProcessor.reset();
            Transaction transaction = new DepositTransaction(amount, this, session);
            if(queueTransaction == false) {
                if(pendingTransactions.isEmpty()) {
                    transactionsProcessor.setTransaction(transaction);
                    transactionsProcessor.start();
                } else {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                    transactionsProcessor.setTransaction(pendingTransactions.get(0));
                    transactionsProcessor.start();
                    resetPendingTransactions();
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
    public void makeTransferBetweenAccounts(Double amount, Account otherAccount, boolean queueTransaction,
                                            Session session, TransactionsProcessor transactionsProcessor) {
        if(!transactionsProcessor.isRunning()) {
            transactionsProcessor.reset();
            Transaction transaction = new TransferBetweenAccountsTransaction(new TransferBetweenAccountsTransactionContext(amount, otherAccount, this, session));
            if(queueTransaction == false) {
                if(pendingTransactions.isEmpty()) {
                    transactionsProcessor.setTransaction(transaction);
                    transactionsProcessor.start();
                } else {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                    transactionsProcessor.setTransaction(pendingTransactions.get(0));
                    transactionsProcessor.start();
                    resetPendingTransactions();
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
    public void makeTransferBetweenBanks(Double amount, String otherAccountIban, String otherAccountNumber,
                                         boolean queueTransaction, Session session, IBankMediator iBankMediator,
                                         TransactionsProcessor transactionsProcessor) {
        if(!transactionsProcessor.isRunning()) {
            transactionsProcessor.reset();
            Transaction transaction = new TransferBetweenBanksTransaction(new TransferBetweenBanksTransactionContext(amount, otherAccountIban, otherAccountNumber, this, session, iBankMediator));
            if(queueTransaction == false) {
                if(pendingTransactions.isEmpty()) {
                    transactionsProcessor.setTransaction(transaction);
                    transactionsProcessor.start();
                } else {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                    transactionsProcessor.setTransaction(pendingTransactions.get(0));
                    transactionsProcessor.start();
                    resetPendingTransactions();
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
    public void makeWithdraw(Double amount, boolean queueTransaction, Session session,
                             TransactionsProcessor transactionsProcessor) {
        if(!transactionsProcessor.isRunning()) {
            transactionsProcessor.reset();
            Transaction transaction = new WithdrawTransaction(amount, this, session);
            if(queueTransaction == false) {
                if(pendingTransactions.isEmpty()) {
                    transactionsProcessor.setTransaction(transaction);
                    transactionsProcessor.start();
                } else {
                    pendingTransactions.get(pendingTransactions.size() - 1).setNextTransaction(transaction);
                    transactionsProcessor.setTransaction(pendingTransactions.get(0));
                    transactionsProcessor.start();
                    resetPendingTransactions();
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
        accountVisitor.visit(this);
    }

}

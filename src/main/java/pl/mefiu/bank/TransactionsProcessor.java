package pl.mefiu.bank;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public final class TransactionsProcessor extends Service<Void> {

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionsProcessor() {}

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                if(transaction != null) {
                    transaction.execute();
                } else {
                    throw new IllegalStateException("there is no transaction(s)!");
                }
                return null;
            }
        };
    }

    private Transaction transaction;

}

package pl.mefiu.bank;

import java.util.List;

public interface IAccountRepository {

    Account accountByAccountNumber(final String accountNumber);

    List<Account> fetchAll();

}

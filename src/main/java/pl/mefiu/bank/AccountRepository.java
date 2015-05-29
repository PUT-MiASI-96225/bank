package pl.mefiu.bank;

import java.util.List;

public final class AccountRepository extends Repository<Account> implements IAccountRepository {

    @Override
    public Account accountByAccountNumber(String accountNumber) {
        if((accountNumber == null) || (accountNumber.isEmpty())) {
            throw new IllegalArgumentException("accountNumber cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = accountNumber;
        return read("from Account account where account.accountNumber = ?", params).get(0);
    }

    @Override
    public List<Account> fetchAll() {
        return read("from Account", null);
    }

}

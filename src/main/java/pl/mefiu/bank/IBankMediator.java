package pl.mefiu.bank;

public interface IBankMediator {

    void transfer(String iban, String accountNumber, Double amount);

    String registerBank(IBank IBank);

}

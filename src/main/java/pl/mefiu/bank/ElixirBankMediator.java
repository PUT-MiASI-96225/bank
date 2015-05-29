package pl.mefiu.bank;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public final class ElixirBankMediator implements IBankMediator {

    private static final Logger log = Logger.getLogger(ElixirBankMediator.class.getName());

    private final Map<String, IBank> banks = new Hashtable<>();

    public ElixirBankMediator() {
        log.info("Creating ElixirBankMediator ...");
        log.info("Created ElixirBankMediator!");
    }

    @Override
    public void transfer(String iban, String accountNumber, Double amount) {
        if(amount > 20000.0) {
            log.info("Transaction over 20000! Transaction recorded!");
        }
        log.info("Transferring " + amount + " to: " + iban + "/" + accountNumber + " ...");
        IBank targetIBank = banks.get(iban);
        if(targetIBank == null) {
            log.info("Transferring " + amount + " to: " + iban + "/" + accountNumber + " ... failed");
            throw new IllegalStateException("there is no such bank!");
        }
        targetIBank.acceptPayload(amount, accountNumber);
        log.info("Transferring " + amount + " to: " + iban + "/" + accountNumber + " ... success");
    }

    @Override
    public String registerBank(IBank ibank) {
        String iban = ibank.getClass().getSimpleName();
        log.info("Registering ibank " + iban + " ...");
        banks.put(iban, ibank);
        log.info("[IBank] IBAN: " + iban);
        log.info("Registering ibank " + iban + " ... success");
        return iban;
    }

    public Map<String, IBank> getBanks() {
        log.info("Getting banks ...");
        for(Map.Entry<String, IBank> stringBankEntry : banks.entrySet()) {
            log.info("[BANK] " + stringBankEntry.getKey());
        }
        log.info("Getting banks ... success");
        return Collections.unmodifiableMap(banks);
    }

}

package org.vitaliyyats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaliyyats.client.FixerCurrencyExchanger;
import org.vitaliyyats.dao.AccountDAO;
import org.vitaliyyats.exception.AccountNotFoundException;
import org.vitaliyyats.exception.CurrencyNotSupportedException;
import org.vitaliyyats.service.AccountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final Logger log = LogManager.getLogger();
    private static final Currency[] currencies = {
            Currency.getInstance("UAH"), 
            Currency.getInstance("USD"),
            Currency.getInstance("EUR"), 
            Currency.getInstance("RUB")};
    private static final AccountService accountService = new AccountService(new FixerCurrencyExchanger(), new AccountDAO());

    public static void main(String[] args){
        //TODO ExecutorService and synchronization
        new Thread(makeRandomTransfers()).start();
    }

    public static Runnable makeRandomTransfers() {
        return () -> {
            while (true) {
                var fromAccIdx = ThreadLocalRandom.current().nextInt(3) + 1;
                var toAccIdx = ThreadLocalRandom.current().nextInt(3) + 1;
                var amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(100));
                var currencyIdx = ThreadLocalRandom.current().nextInt(3);
                try {
                    accountService.transferMoney("Account" + fromAccIdx + ".txt", 
                            "Account" + toAccIdx + ".txt", amount, currencies[currencyIdx]);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
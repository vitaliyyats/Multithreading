package org.vitaliyyats.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaliyyats.Account;
import org.vitaliyyats.exception.AccountNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Currency;
import java.util.Scanner;

public class AccountDAO {
    private static final Logger log = LogManager.getLogger();
    public Account loadFromFile(String fileName) throws AccountNotFoundException {
        var file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter(" ");
            return Account.builder()
                    .username(scanner.next())
                    .accountCurrency(Currency.getInstance(scanner.next()))
                    .balance(new BigDecimal(scanner.next()))
                    .otherSupportedCurrencies(scanner.tokens().map(Currency::getInstance).toList())
                    .build();
        } catch (FileNotFoundException e) {
            // do we need to pass original exception also?
            throw new AccountNotFoundException("Account " + fileName + " not found");
        }
    }
    
    public void saveAccount(Account account, String fileName) throws IOException {
        var fileContent = new StringBuilder();
        fileContent.append(account.getUsername()).append(" ");
        fileContent.append(account.getAccountCurrency()).append(" ");
        fileContent.append(account.getBalance()).append(" ");
        account.getOtherSupportedCurrencies().forEach( curr -> fileContent.append(curr).append(" "));
        Files.write(Paths.get(fileName), fileContent.toString().getBytes());
    }
}
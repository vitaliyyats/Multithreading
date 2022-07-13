package org.vitaliyyats.dao;

import org.vitaliyyats.Account;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Scanner;

public class AccountDAO {
    public Account loadFromFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter(" ");
            return Account.builder()
                    .username(scanner.next())
                    .accountCurrency(Currency.getInstance(scanner.next()))
                    .balance(new BigDecimal(scanner.next()))
                    .otherSupportedCurrencies(scanner.tokens().map(Currency::getInstance).toList())
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

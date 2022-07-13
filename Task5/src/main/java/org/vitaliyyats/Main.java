package org.vitaliyyats;

import org.vitaliyyats.client.FixerCurrencyExchanger;
import org.vitaliyyats.dao.AccountDAO;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        AccountDAO accountDAO = new AccountDAO();
        var account = accountDAO.loadFromFile("Account1.txt");
        System.out.println(account);

        var exchanger = new FixerCurrencyExchanger();
        var convertedValue = exchanger.convert(account.getBalance(), account.getAccountCurrency(), account.getOtherSupportedCurrencies().get(0));
        System.out.println(convertedValue);
    }
}
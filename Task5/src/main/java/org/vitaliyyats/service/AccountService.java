package org.vitaliyyats.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaliyyats.Account;
import org.vitaliyyats.client.FixerCurrencyExchanger;
import org.vitaliyyats.dao.AccountDAO;
import org.vitaliyyats.exception.AccountNotFoundException;
import org.vitaliyyats.exception.CurrencyNotSupportedException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Objects;

public class AccountService {
    private static final Logger log = LogManager.getLogger();
    private final FixerCurrencyExchanger exchanger;

    private final AccountDAO accountDAO;

    public AccountService(FixerCurrencyExchanger exchanger, AccountDAO accountDAO) {
        this.exchanger = exchanger;
        this.accountDAO = accountDAO;
    }

    public void transferMoney(String fromAcc, String toAcc, BigDecimal amount, Currency currency) throws CurrencyNotSupportedException, AccountNotFoundException, URISyntaxException, IOException, InterruptedException {
        if (fromAcc.equals(toAcc)) {
            throw new IllegalArgumentException("From and to accounts are the same - can't transfer money");
        }
        var from = accountDAO.loadFromFile(fromAcc);
        var to = accountDAO.loadFromFile(toAcc);
        validateTransferInputs(from, to, amount, currency);

        BigDecimal amountToTransfer;
        if (from.getAccountCurrency().equals(currency)) {
            checkEnoughMoneyOnBalance(from, amount);
            amountToTransfer = amount;
        } else {
            var convertedAmount = exchanger.convert(amount, currency, from.getAccountCurrency());
            checkEnoughMoneyOnBalance(from, convertedAmount);
            amountToTransfer = convertedAmount;
        }
        
        if (from.getAccountCurrency().equals(to.getAccountCurrency())) {
            transferForEqualCurrency(from, to, amountToTransfer);
        } else {
            var convertedToBalance = exchanger.convert(to.getBalance(), to.getAccountCurrency(), from.getAccountCurrency());
            var newToBalance = convertedToBalance.add(amountToTransfer);
            var newToBalanceInToCurrency = exchanger.convert(newToBalance, from.getAccountCurrency(), to.getAccountCurrency());
            from.setBalance(from.getBalance().subtract(amountToTransfer));
            to.setBalance(newToBalanceInToCurrency);
        }
        log.info("Sent {} {} from {}'s account to {}", amount, currency, from.getUsername(), to.getUsername());
        log.info("{}'s balance is {} {}", from.getUsername(), from.getBalance(), from.getAccountCurrency());
        log.info("{}'s balance is {} {}", to.getUsername(), to.getBalance(), to.getAccountCurrency());
        
        accountDAO.saveAccount(from, fromAcc);
        accountDAO.saveAccount(to, toAcc);
    }

    private void transferForEqualCurrency(Account from, Account to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }

    private void checkEnoughMoneyOnBalance(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough money on " + account.getUsername() + "'s balance to transfer " + amount + " " + account.getAccountCurrency());
        }
    }

    // is it better to create separate validation service?
    private void validateTransferInputs(Account from, Account to, BigDecimal amount, Currency currency) throws CurrencyNotSupportedException {
//      maybe try to replace 2 if with something like this?
//      List.of(from,to).stream()
//                .filter(acc -> !isCurrencySupportedByAccount(acc,currency))
//                ....
        if (!isCurrencySupportedByAccount(from, currency)) {
            throw new CurrencyNotSupportedException("Currency " + currency + " is not supported by " + from.getUsername() + "'s account");
        }
        if (!isCurrencySupportedByAccount(to, currency)) {
            throw new CurrencyNotSupportedException("Currency " + currency + " is not supported by " + to.getUsername() + "'s account");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount can't be less than zero");
        }
    }

    public boolean isCurrencySupportedByAccount(Account account, Currency currency) {
        // what approach should we use here for null checking?
        Objects.requireNonNull(account);
        Objects.requireNonNull(account.getAccountCurrency());
        Objects.requireNonNull(currency);

        if (account.getAccountCurrency().equals(currency)) {
            return true;
        }

        if (account.getOtherSupportedCurrencies() == null) {
            return false;
        }

        return account.getOtherSupportedCurrencies()
                .stream()
                .anyMatch(c -> c.equals(currency));
    }
}

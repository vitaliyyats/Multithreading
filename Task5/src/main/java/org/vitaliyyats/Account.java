package org.vitaliyyats;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Getter
@Builder
@ToString
public class Account {
    private String username;
    private Currency accountCurrency;
    private BigDecimal balance;
    private List<Currency> otherSupportedCurrencies;
}

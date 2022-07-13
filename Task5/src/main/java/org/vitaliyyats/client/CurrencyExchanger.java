package org.vitaliyyats.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Currency;

public interface CurrencyExchanger {
    BigDecimal convert(BigDecimal value, Currency source, Currency target) throws URISyntaxException, IOException, InterruptedException;
}

package org.vitaliyyats.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Currency;

public class FixerCurrencyExchanger implements CurrencyExchanger {

    private static final String FIXER_ACCESS_KEY = "21f3637a7247a9e319429f96fb2c1e9a";

    private static final String FIXER_URI = "http://data.fixer.io/api/";

    private static final Logger log = LogManager.getLogger();

    @Override
    public BigDecimal convert(BigDecimal value, Currency source, Currency target) throws URISyntaxException, IOException, InterruptedException {
        if (source.equals(target)) {
            return value;
        }

        FixerResponse fixerResponse = callFixerService(source, target);
        
        BigDecimal result;
        if (fixerResponse.getBase().equals(source.getCurrencyCode())) {
            var rate = fixerResponse.getRates().get(target.getCurrencyCode());
            result = value.multiply(rate);
        } else {
            var rateToBaseFromSource = fixerResponse.getRates().get(source.getCurrencyCode());
            var rateFromBaseToTarget = fixerResponse.getRates().get(target.getCurrencyCode());
            result = value.setScale(2, RoundingMode.HALF_UP)
                    .divide(rateToBaseFromSource, RoundingMode.HALF_UP)
                    .multiply(rateFromBaseToTarget);
        }
        result = result.setScale(2, RoundingMode.HALF_UP);
        log.info("Converted {} {} to {} {}", value, source.getCurrencyCode(), result, target.getCurrencyCode());
        return result;
    }

    private FixerResponse callFixerService(Currency source, Currency target) throws URISyntaxException, IOException, InterruptedException {
        String currDate = LocalDate.now().toString();
        var uri = new URI(FIXER_URI + currDate + "?access_key=" + FIXER_ACCESS_KEY +
                "&symbols=" + source.getCurrencyCode() + ',' + target.getCurrencyCode());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return convertToFixerResp(response.body());
    }

    private FixerResponse convertToFixerResp(String resp) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.readValue(resp, FixerResponse.class);
    }
}

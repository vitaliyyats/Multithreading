package org.vitaliyyats.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixerResponse {
    boolean success;
    String base;
    Map<String, BigDecimal> rates;
}

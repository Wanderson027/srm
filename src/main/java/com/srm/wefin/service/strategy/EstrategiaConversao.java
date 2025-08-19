package com.srm.wefin.service.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface EstrategiaConversao {
    BigDecimal convert(BigDecimal amount, BigDecimal baseRate, BigDecimal multiplier);
    String name();
}


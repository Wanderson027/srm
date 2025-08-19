package com.srm.wefin.service.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EstrategiaLinear implements EstrategiaConversao {
    @Override
    public BigDecimal convert(BigDecimal valor, BigDecimal taxaBase, BigDecimal multiplicador) {
        return valor.multiply(taxaBase).multiply(multiplicador);
    }

    @Override
    public String name() {
        return "LINEAR";
    }
}
package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequisicaoTaxaCambio(
        String moedaBase,
        String moedaDestino,
        LocalDate data,
        BigDecimal taxa
) {}


package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequisicaoConversao(
        Long produtoId,
        String moedaOrigem,
        String moedaDestino,
        BigDecimal valor,
        LocalDate naData,
        String chaveIdempotencia
) {}
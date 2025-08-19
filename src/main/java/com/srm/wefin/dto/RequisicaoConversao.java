package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequisicaoConversao(
        Long produtoId,
        String moedaOrigem,  // "OR"
        String moedaDestino, // "TIB"
        BigDecimal valor,
        LocalDate naData,    // opcional; default = hoje
        String chaveIdempotencia // opcional
) {}
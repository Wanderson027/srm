package com.srm.wefin.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

// dto/RespostaConversao.java
@Builder
public record RespostaConversao(
        String produto,
        String reino,
        String moedaOrigem,
        String moedaDestino,
        BigDecimal valorOrigem,
        BigDecimal taxaBase,
        BigDecimal multiplicador,
        BigDecimal valorDestino,
        LocalDate dataTaxa,
        OffsetDateTime executadaEm,
        Long idTransacao
) {}

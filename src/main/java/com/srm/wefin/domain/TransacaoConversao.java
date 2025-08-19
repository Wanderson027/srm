package com.srm.wefin.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transacao_conversao",
        indexes = @Index(name = "idx_tx_criada_em", columnList = "criada_em"),
        uniqueConstraints = @UniqueConstraint(name = "uk_idempotencia", columnNames = {"chave_idempotencia"}))
@Data
@NoArgsConstructor
public class TransacaoConversao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "criada_em", nullable = false)
    private OffsetDateTime criadaEm = OffsetDateTime.now();

    @ManyToOne(optional = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moeda_origem", referencedColumnName = "codigo")
    private Moeda moedaOrigem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moeda_destino", referencedColumnName = "codigo")
    private Moeda moedaDestino;

    @ManyToOne(optional = false)
    private Reino reino;

    @Column(name = "valor_origem", nullable = false, precision = 18, scale = 6)
    private BigDecimal valorOrigem;

    @Column(name = "taxa_utilizada", nullable = false, precision = 18, scale = 6)
    private BigDecimal taxaUtilizada;

    @Column(name = "multiplicador_utilizado", nullable = false, precision = 18, scale = 6)
    private BigDecimal multiplicadorUtilizado;

    @Column(name = "valor_destino", nullable = false, precision = 18, scale = 6)
    private BigDecimal valorDestino;

    @Column(name = "data_taxa", nullable = false)
    private LocalDate dataTaxa;

    @Column(name = "chave_idempotencia", length = 80)
    private String chaveIdempotencia;

}


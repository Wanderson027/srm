package com.srm.wefin.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "taxa_cambio",
        uniqueConstraints = @UniqueConstraint(name = "uk_taxa_dia", columnNames = {"moeda_base", "moeda_destino", "data_taxa"}))
@Data
@NoArgsConstructor
public class TaxaCambio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moeda_base", referencedColumnName = "codigo")
    private Moeda moedaBase;

    @ManyToOne(optional = false)
        @JoinColumn(name = "moeda_destino", referencedColumnName = "codigo")
    private Moeda moedaDestino;

    @Column(name = "data_taxa", nullable = false)
    private LocalDate dataTaxa;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal taxa; // ex: 1 OR = 2.5 TIB
}


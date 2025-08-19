package com.srm.wefin.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "regra_conversao_produto",
        uniqueConstraints = @UniqueConstraint(name = "uk_regra", columnNames = {"produto_id", "moeda_base", "moeda_destino"}))
@Data
@NoArgsConstructor
public class RegraConversaoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moeda_base", referencedColumnName = "codigo")
    private Moeda moedaBase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moeda_destino", referencedColumnName = "codigo")
    private Moeda moedaDestino;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal multiplicador = BigDecimal.ONE;

    @Column(name = "tipo_formula")
    private String tipoFormula;

    @Column(name = "parametros_formula", columnDefinition = "text")
    private String parametrosFormula;
}


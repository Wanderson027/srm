package com.srm.wefin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "moeda")
@NoArgsConstructor
public class Moeda {
    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, unique = true)
    private String nome;
}

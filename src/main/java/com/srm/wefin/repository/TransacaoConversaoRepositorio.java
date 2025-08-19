package com.srm.wefin.repository;

import com.srm.wefin.domain.TransacaoConversao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransacaoConversaoRepositorio extends JpaRepository<TransacaoConversao, Long> {
    boolean existsByChaveIdempotencia(String chave);
    Optional<TransacaoConversao> findByChaveIdempotencia(String chave);
}
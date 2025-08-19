package com.srm.wefin.repository;

import com.srm.wefin.domain.TaxaCambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TaxaCambioRepositorio extends JpaRepository<TaxaCambio, Long> {
    Optional<TaxaCambio> findTopByMoedaBaseCodigoAndMoedaDestinoCodigoAndDataTaxa(
            String moedaBase, String moedaDestino, LocalDate dataTaxa);
}
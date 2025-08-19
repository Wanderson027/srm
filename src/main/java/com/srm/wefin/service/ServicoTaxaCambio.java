package com.srm.wefin.service;

import com.srm.wefin.domain.TaxaCambio;
import com.srm.wefin.dto.RequisicaoTaxaCambio;
import com.srm.wefin.repository.MoedaRepositorio;
import com.srm.wefin.repository.TaxaCambioRepositorio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class ServicoTaxaCambio {
    private final TaxaCambioRepositorio repositorio;
    private final MoedaRepositorio moedaRepositorio;

    public TaxaCambio buscarTaxa(String base, String destino, LocalDate data) {
        return repositorio.findTopByMoedaBaseCodigoAndMoedaDestinoCodigoAndDataTaxa(base, destino, data)
                .orElseThrow(() -> new NoSuchElementException("Taxa de cambio nao encontrada"));
    }

    @Transactional
    public TaxaCambio salvarOuAtualizar(RequisicaoTaxaCambio r) {
        var base = moedaRepositorio.findById(r.moedaBase()).orElseThrow();
        var destino = moedaRepositorio.findById(r.moedaDestino()).orElseThrow();
        var existente = repositorio.findTopByMoedaBaseCodigoAndMoedaDestinoCodigoAndDataTaxa(
                r.moedaBase(), r.moedaDestino(), r.data()).orElse(null);

        var taxaCambio = Optional.ofNullable(existente).orElseGet(TaxaCambio::new);
        taxaCambio.setMoedaBase(base);
        taxaCambio.setMoedaDestino(destino);
        taxaCambio.setDataTaxa(r.data());
        taxaCambio.setTaxa(r.taxa());
        return repositorio.save(taxaCambio);
    }
}
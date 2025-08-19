package com.srm.wefin.api;

import com.srm.wefin.domain.TaxaCambio;
import com.srm.wefin.dto.RequisicaoTaxaCambio;
import com.srm.wefin.service.ServicoTaxaCambio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/taxas-cambio")
@RequiredArgsConstructor
public class ControladorTaxaCambio {
    private final ServicoTaxaCambio servico;

    @GetMapping
    public ResponseEntity<TaxaCambio> buscar(
            @RequestParam String base,
            @RequestParam String destino,
            @RequestParam LocalDate data) {
        return ResponseEntity.ok(servico.buscarTaxa(base, destino, data));
    }

    @PostMapping
    public ResponseEntity<TaxaCambio> salvarOuAtualizar(@Valid @RequestBody RequisicaoTaxaCambio req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.salvarOuAtualizar(req));
    }
}

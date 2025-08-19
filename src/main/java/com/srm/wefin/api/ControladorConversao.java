package com.srm.wefin.api;

import com.srm.wefin.dto.RequisicaoConversao;
import com.srm.wefin.dto.RespostaConversao;
import com.srm.wefin.service.ServicoConversao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/conversoes")
@RequiredArgsConstructor
public class ControladorConversao {
    private final ServicoConversao servico;

    @PostMapping
    public ResponseEntity<RespostaConversao> converter(@Valid @RequestBody RequisicaoConversao req) {
        return ResponseEntity.ok(servico.converter(req));
    }
}

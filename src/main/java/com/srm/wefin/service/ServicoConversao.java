package com.srm.wefin.service;

import com.srm.wefin.domain.RegraConversaoProduto;
import com.srm.wefin.domain.TransacaoConversao;
import com.srm.wefin.dto.RequisicaoConversao;
import com.srm.wefin.dto.RespostaConversao;
import com.srm.wefin.repository.*;
import com.srm.wefin.service.strategy.EstrategiaConversao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoConversao {

    private final MoedaRepositorio moedaRepo;
    private final ProdutoRepositorio produtoRepo;
    private final TaxaCambioRepositorio taxaRepo;
    private final RegraConversaoProdutoRepositorio regraRepo;
    private final TransacaoConversaoRepositorio txRepo;
    private final List<EstrategiaConversao> estrategias;

    @Transactional
    public RespostaConversao converter(RequisicaoConversao req) {
        var produto = produtoRepo.findById(req.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto inexistente"));

        var origem = moedaRepo.findById(req.moedaOrigem())
                .orElseThrow(() -> new IllegalArgumentException("Moeda de origem inexistente"));
        var destino = moedaRepo.findById(req.moedaDestino())
                .orElseThrow(() -> new IllegalArgumentException("Moeda de destino inexistente"));

        var data = Optional.ofNullable(req.naData()).orElse(LocalDate.now());

        var taxa = taxaRepo.findTopByMoedaBaseCodigoAndMoedaDestinoCodigoAndDataTaxa(origem.getCodigo(), destino.getCodigo(), data)
                .orElseThrow(() -> new IllegalStateException("Taxa de cambio nao encontrada para a data"));

        var regra = regraRepo.findByProdutoIdAndMoedaBaseCodigoAndMoedaDestinoCodigo(produto.getId(), origem.getCodigo(), destino.getCodigo())
                .orElseGet(() -> {
                    var r = new RegraConversaoProduto();
                    r.setProduto(produto); r.setMoedaBase(origem); r.setMoedaDestino(destino);
                    r.setMultiplicador(BigDecimal.ONE); r.setTipoFormula("LINEAR");
                    return r;
                });

        var estrategia = estrategias.stream()
                .filter(e -> e.name().equalsIgnoreCase(Optional.ofNullable(regra.getTipoFormula()).orElse("LINEAR")))
                .findFirst().orElseThrow(() -> new IllegalStateException("Estrategia nao suportada"));

        var valorDestino = estrategia.convert(req.valor(), taxa.getTaxa(), regra.getMultiplicador())
                .setScale(4, RoundingMode.HALF_UP);

        if (req.chaveIdempotencia() != null && txRepo.existsByChaveIdempotencia(req.chaveIdempotencia())) {
            var existente = txRepo.findByChaveIdempotencia(req.chaveIdempotencia()).get();
            return RespostaConversao.builder()
                    .produto(produto.getNome()).reino(produto.getReino().getNome())
                    .moedaOrigem(origem.getCodigo()).moedaDestino(destino.getCodigo())
                    .valorOrigem(existente.getValorOrigem()).taxaBase(existente.getTaxaUtilizada())
                    .multiplicador(existente.getMultiplicadorUtilizado()).valorDestino(existente.getValorDestino())
                    .dataTaxa(existente.getDataTaxa()).executadaEm(existente.getCriadaEm())
                    .idTransacao(existente.getId()).build();
        }

        var tx = new TransacaoConversao();
        tx.setProduto(produto); tx.setReino(produto.getReino());
        tx.setMoedaOrigem(origem); tx.setMoedaDestino(destino);
        tx.setValorOrigem(req.valor()); tx.setTaxaUtilizada(taxa.getTaxa());
        tx.setMultiplicadorUtilizado(regra.getMultiplicador()); tx.setValorDestino(valorDestino);
        tx.setDataTaxa(data); tx.setChaveIdempotencia(req.chaveIdempotencia());
        txRepo.save(tx);

        return RespostaConversao.builder()
                .produto(produto.getNome()).reino(produto.getReino().getNome())
                .moedaOrigem(origem.getCodigo()).moedaDestino(destino.getCodigo())
                .valorOrigem(req.valor()).taxaBase(taxa.getTaxa())
                .multiplicador(regra.getMultiplicador()).valorDestino(valorDestino)
                .dataTaxa(data).executadaEm(tx.getCriadaEm()).idTransacao(tx.getId())
                .build();
    }
}

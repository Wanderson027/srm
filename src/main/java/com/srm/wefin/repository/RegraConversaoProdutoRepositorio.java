package com.srm.wefin.repository;

import com.srm.wefin.domain.RegraConversaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegraConversaoProdutoRepositorio extends JpaRepository<RegraConversaoProduto, Long> {
    Optional<RegraConversaoProduto> findByProdutoIdAndMoedaBaseCodigoAndMoedaDestinoCodigo(
            Long produtoId, String moedaBase, String moedaDestino);
}
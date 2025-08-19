package com.srm.wefin.repository;

import com.srm.wefin.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {}
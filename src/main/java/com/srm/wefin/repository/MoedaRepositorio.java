package com.srm.wefin.repository;

import com.srm.wefin.domain.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoedaRepositorio extends JpaRepository<Moeda, String> {}
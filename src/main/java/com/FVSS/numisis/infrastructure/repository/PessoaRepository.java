package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}

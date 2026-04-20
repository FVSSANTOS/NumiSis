package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.DadosBancarios;

public interface DadosBancariosRepository extends JpaRepository<DadosBancarios, Long> {
}

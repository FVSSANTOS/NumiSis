package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}

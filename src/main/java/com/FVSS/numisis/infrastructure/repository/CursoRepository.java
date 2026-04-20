package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}

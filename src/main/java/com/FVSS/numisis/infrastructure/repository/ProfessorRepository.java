package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}

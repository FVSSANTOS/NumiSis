package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}

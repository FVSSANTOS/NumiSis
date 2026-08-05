package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.HistoricoDisciplina;

public interface HistoricoDisciplinaRepository extends JpaRepository<HistoricoDisciplina, Long> {

    Page<HistoricoDisciplina> findByAlunoId(Long alunoId, Pageable pageable);
}

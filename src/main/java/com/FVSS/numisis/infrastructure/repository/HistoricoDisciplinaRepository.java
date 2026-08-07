package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FVSS.numisis.domain.model.HistoricoDisciplina;

public interface HistoricoDisciplinaRepository extends JpaRepository<HistoricoDisciplina, Long> {

    // @Query explícito: uma query derivada findByAlunoId colidiria com o getter transient
    // getAlunoId() (Hibernate tenta resolver "alunoId" como atributo persistente e falha em
    // runtime). Mesmo problema já visto em CursoDisciplinaRepository — ver SKILL.md.
    @Query(value = "SELECT hd FROM HistoricoDisciplina hd JOIN FETCH hd.aluno JOIN FETCH hd.turma WHERE hd.aluno.id = :alunoId",
            countQuery = "SELECT COUNT(hd) FROM HistoricoDisciplina hd WHERE hd.aluno.id = :alunoId")
    Page<HistoricoDisciplina> findByAlunoId(@Param("alunoId") Long alunoId, Pageable pageable);

    @Query(value = "SELECT hd FROM HistoricoDisciplina hd JOIN FETCH hd.aluno JOIN FETCH hd.turma WHERE hd.turma.id = :turmaId",
            countQuery = "SELECT COUNT(hd) FROM HistoricoDisciplina hd WHERE hd.turma.id = :turmaId")
    Page<HistoricoDisciplina> findByTurmaId(@Param("turmaId") Long turmaId, Pageable pageable);
}

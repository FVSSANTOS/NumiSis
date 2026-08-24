package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FVSS.numisis.domain.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    @Query(value = "SELECT DISTINCT t FROM Turma t LEFT JOIN t.historicos hd WHERE t.professor.id = :pessoaId OR hd.aluno.id = :pessoaId",
            countQuery = "SELECT COUNT(DISTINCT t) FROM Turma t LEFT JOIN t.historicos hd WHERE t.professor.id = :pessoaId OR hd.aluno.id = :pessoaId")
    Page<Turma> findByPessoaId(@Param("pessoaId") Long pessoaId, Pageable pageable);
}

package com.FVSS.numisis.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByCpf(String cpf);

    Optional<Aluno> findByUsuarioId(Long usuarioId);
}

package com.FVSS.numisis.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FVSS.numisis.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

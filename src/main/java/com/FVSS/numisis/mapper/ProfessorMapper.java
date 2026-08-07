package com.FVSS.numisis.mapper;

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.dto.ProfessorDTO;

public class ProfessorMapper {

    public static ProfessorDTO toDTO(Professor entity) {
        if (entity == null) return null;

        String usuarioLogin = entity.getUsuario() != null ? entity.getUsuario().getLogin() : null;
        long usuarioID = entity.getUsuario() != null ? entity.getUsuario().getId() : null;
        ProfessorDTO dto = new ProfessorDTO(
            entity.getId(),
            entity.getNome(),
            entity.getCpf(),
            entity.getIdade(),
            entity.getDataNascimento(),
            entity.getEmail(),
            entity.getCargaHoraria(),
            entity.getEndereco(),
            entity.getTelefones(),
            entity.getDadosBancarios(),
            entity.getTurmas(),
            usuarioLogin,
            usuarioID
        );
        return dto;
    }
}

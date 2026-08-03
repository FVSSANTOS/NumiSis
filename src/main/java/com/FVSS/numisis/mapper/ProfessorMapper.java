package com.FVSS.numisis.mapper;

import com.FVSS.numisis.domain.model.Professor;
import com.FVSS.numisis.dto.ProfessorDTO;

public class ProfessorMapper {

    public static ProfessorDTO toDTO(Professor entity) {
        if (entity == null) return null;

        ProfessorDTO dto = new ProfessorDTO(
            entity.getId(), 
            entity.getNome(), 
            entity.getCpf(), 
            entity.getIdade(), 
            entity.getCargaHoraria());
        return dto;
    }
}

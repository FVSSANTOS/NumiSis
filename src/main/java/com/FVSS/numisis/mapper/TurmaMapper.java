package com.FVSS.numisis.mapper;

import com.FVSS.numisis.domain.model.Turma;
import com.FVSS.numisis.dto.TurmaDTO;

public class TurmaMapper {

    public static TurmaDTO toDTO(Turma entity) {
        if (entity == null) return null;

        TurmaDTO dto = new TurmaDTO(
            entity.getId(), 
            entity.getAno(), 
            entity.getSemestre(), 
            entity.getSala(), 
            entity.getHorarioInicio(), 
            entity.getHorarioTermino(), 
            entity.getDisciplina().getId(), 
            entity.getProfessor().getId());
        return dto;
    }
}

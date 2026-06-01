package com.FVSS.numisis.mapper;

import com.FVSS.numisis.domain.model.Disciplina;
import com.FVSS.numisis.dto.DisciplinaDTO;

public class DisciplinaMapper {

    public static DisciplinaDTO toDTO(Disciplina entity) {
        if (entity == null) return null;
        DisciplinaDTO dto = new DisciplinaDTO(entity.getId(), entity.getNome(), entity.getDescricao());
        return dto;
    }
}
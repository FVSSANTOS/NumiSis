package com.FVSS.numisis.dto;

import com.FVSS.numisis.domain.model.Professor;

public class ProfessorMapper {

    public static ProfessorDTO toDTO(Professor entity) {
        if (entity == null) return null;

        ProfessorDTO dto = new ProfessorDTO(
            entity.getId(), 
            entity.getNome(), 
            entity.getCpf(), 
            entity.getIdade(), 
            entity.getCargaHoraria(), 
            entity.getDadosBancarios());
        return dto;
    }
}

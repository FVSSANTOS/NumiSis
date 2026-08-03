package com.FVSS.numisis.mapper;

import java.util.List;
import java.util.Objects;

import com.FVSS.numisis.domain.model.Aluno;
import com.FVSS.numisis.domain.model.Curso;
import com.FVSS.numisis.domain.model.Matricula;
import com.FVSS.numisis.dto.AlunoDTO;


public class AlunoMapper {

    public static AlunoDTO toDTO(Aluno entity) {
        if (entity == null) return null;

        List<String> cursos = entity.getMatriculas().stream()
            .map(Matricula::getCurso)
            .filter(Objects::nonNull)
            .map(Curso::getNome)
            .distinct()
            .toList();

        AlunoDTO dto = new AlunoDTO(
            entity.getId(),
            entity.getNome(),
            entity.getCpf(),
            entity.getIdade(),
            entity.getDataNascimento(),
            entity.getNomePai(),
            entity.getNomeMae(),
            entity.getTelefones(),
            entity.getEmail(),
            entity.getCondicaoEspecial(),
            entity.getAlergia(),
            cursos
        );
        return dto;
    }
}

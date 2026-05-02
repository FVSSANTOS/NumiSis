package com.FVSS.numisis.dto;

import com.FVSS.numisis.domain.model.DadosBancarios;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String cpf;
    private Integer idade;

    private String cargaHoraria;

    private DadosBancarios dadosBancarios;
}

package com.FVSS.numisis.dto;

import java.time.LocalDate;
import java.util.List;

import com.FVSS.numisis.domain.model.Telefone;

public record AlunoDTO(
     long id,
     String nome,
     String cpf,
     int idade,
     LocalDate dataNascimento,
     String nomePai,
     String nomeMae,
     List<Telefone> telefones,
     String email,
     String condicaoEspecial,
     String alergia,
     List<String> cursos
) {}

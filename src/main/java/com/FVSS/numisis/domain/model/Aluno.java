package com.FVSS.numisis.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
public class Aluno extends Pessoa {
	
	@Column(name = "nome_mae")
	private String nomeMae;

	@Column(name = "nome_pai")
	private String nomePai;

	@Column(name = "condicao_especial")
	@NotBlank
	private String condicaoEspecial;

	@Column(name = "alergia")
	@NotBlank
	private String alergia;

	@OneToMany(mappedBy = "aluno")
	private List<Matricula> matriculas = new ArrayList<>();

	@OneToMany(mappedBy = "aluno")
	private List<HistoricoDisciplina> historicos = new ArrayList<>();

	public Aluno(String nomeMae, String nomePai, String condicaoEspecial, String alergia) {
		super();
		this.alergia = alergia;
		this.condicaoEspecial = condicaoEspecial;
		this.nomeMae = nomeMae;
		this.nomePai = nomePai;
	}

}

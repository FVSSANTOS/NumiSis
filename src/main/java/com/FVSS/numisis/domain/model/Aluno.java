package com.FVSS.numisis.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
public class Aluno extends Pessoa {
	

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Column(name = "idade")
	private Integer idade;

	@Column(name = "nome_mae")
	private String nomeMae;

	@Column(name = "nome_pai")
	private String nomePai;

	@Column(name = "condicao_especial")
	private String condicaoEspecial;

	@Column(name = "alergia")
	private String alergia;

	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;

	@OneToOne
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	@OneToMany(mappedBy = "aluno")
	private List<Matricula> matriculas = new ArrayList<>();

	@OneToMany(mappedBy = "aluno")
	private List<HistoricoDisciplina> historicos = new ArrayList<>();

	public Aluno() {
		super();
	}

}

package com.FVSS.numisis.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "turma")
@Getter
@Setter
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ano")
	private Integer ano;

	@Column(name = "semestre")
	private Integer semestre;

	@Column(name = "sala")
	private String sala;

	@Column(name = "horario_inicio")
	private String horarioInicio;

	@Column(name = "horario_termino")
	private String horarioTermino;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disciplina_id")
	private Disciplina disciplina;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "professor_id")
	private Professor professor;

	@OneToMany(mappedBy = "turma")
	private List<HistoricoDisciplina> historicos = new ArrayList<>();

	public Turma() {
	}

	// Ids/nomes derivados, sem desfazer o write-only de `disciplina`/`professor` acima.
	@Transient
	public Long getDisciplinaId() {
		return disciplina != null ? disciplina.getId() : null;
	}

	@Transient
	public String getDisciplinaNome() {
		return disciplina != null ? disciplina.getNome() : null;
	}

	@Transient
	public Long getProfessorId() {
		return professor != null ? professor.getId() : null;
	}

	@Transient
	public String getProfessorNome() {
		return professor != null ? professor.getNome() : null;
	}
}

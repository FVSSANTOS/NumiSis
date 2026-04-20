package com.FVSS.numisis.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "disciplina")
@Getter
@Setter
public class Disciplina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	@OneToMany(mappedBy = "disciplina")
	private List<CursoDisciplina> cursoDisciplinas = new ArrayList<>();

	@OneToMany(mappedBy = "disciplina")
	private List<Turma> turmas = new ArrayList<>();

	public Disciplina() {
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas != null ? turmas : new ArrayList<>();
	}

}

package com.ufcg.psoft.scrumboard.dto;

import java.util.List;

public class ProjetoDTO {

	private int id;

	private String nome;

	private String descricao;

	private String instituicaoParceira;

	private List<PapelDTO> membros;

	public ProjetoDTO(int id, String nome, String descricao,
			String instituicaoParceira, List<PapelDTO> membros) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.instituicaoParceira = instituicaoParceira;
		this.membros = membros;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getInstituicaoParceira() {
		return instituicaoParceira;
	}

	public List<PapelDTO> getMembros() {
		return membros;
	}

}

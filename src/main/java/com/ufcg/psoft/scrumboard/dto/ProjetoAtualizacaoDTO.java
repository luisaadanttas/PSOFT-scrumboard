package com.ufcg.psoft.scrumboard.dto;

public class ProjetoAtualizacaoDTO {

	private String nome;

	private String descricao;

	private String instituicaoParceira;

	public ProjetoAtualizacaoDTO(String nome, String descricao, String instituicaoParceira) {
		this.nome = nome;
		this.descricao = descricao;
		this.instituicaoParceira = instituicaoParceira;
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

}

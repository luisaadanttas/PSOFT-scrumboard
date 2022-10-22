package com.ufcg.psoft.scrumboard.dto;

public class ProjetoCriacaoDTO {

	private String nome;

	private String descricao;

	private String instituicaoParceira;

	private String usernameDoScrumMaster;

	public ProjetoCriacaoDTO(String nome, String descricao,
			String instituicaoParceira, String usernameDoScrumMaster) {
		this.nome = nome;
		this.descricao = descricao;
		this.instituicaoParceira = instituicaoParceira;
		this.usernameDoScrumMaster = usernameDoScrumMaster;
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

	public String getUsernameDoScrumMaster() {
		return usernameDoScrumMaster;
	}

}

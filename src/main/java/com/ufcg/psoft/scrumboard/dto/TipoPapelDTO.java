package com.ufcg.psoft.scrumboard.dto;

public class TipoPapelDTO {

	private String id;

	private String nome;

	public TipoPapelDTO(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

}

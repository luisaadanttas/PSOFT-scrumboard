package com.ufcg.psoft.scrumboard.dto;

public class ProjetoDTOBasico {

	private int id;

	private String nome;

	private String scrumMaster;

	public ProjetoDTOBasico(int id, String nome, String scrumMaster) {
		this.id = id;
		this.nome = nome;
		this.scrumMaster = scrumMaster;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getScrumMaster() {
		return scrumMaster;
	}

}

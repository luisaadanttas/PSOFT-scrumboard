package com.ufcg.psoft.scrumboard.model;


public class User {
	private String email;
	private String nome;
	private String username;


	public User(String email, String nome, String username){
		this.email = email;
		this.nome = nome;
		this.username = username;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail() {
		return this.email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
}
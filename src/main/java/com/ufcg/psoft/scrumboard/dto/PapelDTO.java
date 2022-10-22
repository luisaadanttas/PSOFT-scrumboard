package com.ufcg.psoft.scrumboard.dto;

public class PapelDTO {

	private String user;

	private String papel;

	public PapelDTO(String user, String papel) {
		this.user = user;
		this.papel = papel;
	}

	public String getUser() {
		return user;
	}

	public String getPapel() {
		return papel;
	}

}

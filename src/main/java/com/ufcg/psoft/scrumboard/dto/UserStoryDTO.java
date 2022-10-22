package com.ufcg.psoft.scrumboard.dto;

public class UserStoryDTO {
	private String title;
	private String description;
	private int projetoId;
	
	public UserStoryDTO(String title, String description, int projetoId) {
		this.title = title;
		this.description = description;
		this.projetoId = projetoId;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getProjetoId() {
		return this.projetoId;
	}
}

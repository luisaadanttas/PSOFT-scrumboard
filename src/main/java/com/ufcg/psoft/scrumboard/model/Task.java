package com.ufcg.psoft.scrumboard.model;

public class Task {
	private final int id;
	private String title;
	private String description;
	private boolean finished;
	
	public Task(int id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.finished = false;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setFinished() {
		this.finished = true;
	}
	
	public boolean isFinished() {
		return this.finished;
	}
	
	public int getId() {
		return this.id;
	}

	public void setTitulo(String titulo){
		this.title= titulo;
	}

	public void setDescricao(String descricao){
		this.description = descricao;
	}

	public String getDescription(){
		return this.description;
	}
}


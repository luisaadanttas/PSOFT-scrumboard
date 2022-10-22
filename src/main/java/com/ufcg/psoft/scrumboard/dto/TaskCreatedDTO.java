package com.ufcg.psoft.scrumboard.dto;

public class TaskCreatedDTO {

    private int id;
    private String title;
    private String description;
    private boolean isFinished;

    public TaskCreatedDTO(int id, String titulo, String descricao, boolean isFinished){
        this.id = id;
        this.title = titulo;
        this.description = descricao;
        this.isFinished = isFinished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return title;
    }

    public String getDescricao() {
        return description;
    }

    public boolean isFinished() {
        return isFinished;
    }
}

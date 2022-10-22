package com.ufcg.psoft.scrumboard.dto;

public class TaskDTO {

    String titulo;
    String descricao;

    public TaskDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }


    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }


}

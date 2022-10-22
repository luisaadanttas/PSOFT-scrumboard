package com.ufcg.psoft.scrumboard.dto;

public class UserDTO {

    private String nome;
    private String username;
    private String email;

    public UserDTO(String email, String nome, String username){
        this.email = email;
        this.nome = nome;
        this.username = username;
    }



    public String getNome() {
        return this.nome;
    }
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }
}
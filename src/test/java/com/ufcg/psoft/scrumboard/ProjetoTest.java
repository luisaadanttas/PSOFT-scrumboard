package com.ufcg.psoft.scrumboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ufcg.psoft.scrumboard.dto.*;
import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class ProjetoTest {

    @Autowired
    private ProjetoController projetoController;
    
    private ProjetoCriacaoDTO projetoDTO;


    @BeforeEach
    public void setUp() {
        projetoDTO = new ProjetoCriacaoDTO("yara","descricao","nubank","joao");
    }

    @Test
    public void testCadastrarProjeto_Success() throws UserException {
        ResponseEntity<?> response = projetoController.cadastraProjeto(projetoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Novo projeto cadastrado com ID 1", response.getBody());
    }

}

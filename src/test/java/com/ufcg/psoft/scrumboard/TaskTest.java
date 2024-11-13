package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.*;
import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.service.*;
import java.util.List;
import com.ufcg.psoft.scrumboard.model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class TaskTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService; 

    @Autowired
    private ProjetoService projetoService; 
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserStoryService userStoryService; 
    
    private TipoPapel tipoPapel; 
    private ProjetoCriacaoDTO projetoDTO;
    private PapelDTO papelDTO;
    private TaskDTO taskDTO;
    private UserStoryDTO userStoryDTO;
    private UserDTO userDTO;


    @BeforeEach
    public void setUp() {
        projetoDTO = new ProjetoCriacaoDTO("yara","descricao","nubank","joao");
        taskDTO = new TaskDTO("t1","script para validacao de cpf");
        userStoryDTO = new UserStoryDTO("t1","script para validar email",1);
        userDTO = new UserDTO("email", "password", "maria");        
    }

    @Test
    public void removeTask_set_success() throws TaskNotFoundException, UserStoryNotFoundException, UserException, NonexistentProjectException, OperationException, InvalidUSRequestException {

        userService.addUser(userDTO);
        projetoService.cadastraProjeto(projetoDTO);
        projetoService.alocaUserEmProjeto(1, "maria", TipoPapel.PESQUISADOR, "joao");
        userStoryService.cadastraUS(userStoryDTO, "joao");
        taskService.cadastraTask(1, 1, taskDTO, "joao");
        taskService.removeTask(1, 1, 1, "joao");
    }


}

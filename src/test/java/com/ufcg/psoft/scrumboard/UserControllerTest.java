package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.repository.UserRepository;
import com.ufcg.psoft.scrumboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService; 
    
    @Autowired
    private UserRepository userRepository; 

    private UserDTO validUserDTO;
    private UserDTO existingUserDTO;

    @BeforeEach
    public void setUp() {
        validUserDTO = new UserDTO("email", "validPassword", "validUsername");
        existingUserDTO = new UserDTO("existingUsername", "password", "email");
    }

    @Test
    public void testCadastrarUser_Success() throws UserException {
        ResponseEntity<?> response = userController.cadastrarUser(validUserDTO);
        System.out.println(userService.getUser("validUsername").getEmail());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuario cadastrado com username: validUsername", response.getBody());

        assertNotNull(userRepository.getUser("validUsername"));
    }
}

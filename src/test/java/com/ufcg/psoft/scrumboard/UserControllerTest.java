package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDTO validUserDTO;
    private UserDTO existingUserDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validUserDTO = new UserDTO("validUsername", "validPassword", "validEmail");
        existingUserDTO = new UserDTO("existingUsername", "password", "email");
    }

    @Test
    public void testCadastrarUser_Success() throws UserException {
        // Mocking service to return a valid username
        when(userService.addUser(validUserDTO)).thenReturn("validUsername");

        // Calling the controller method
        ResponseEntity<?> response = userController.cadastrarUser(validUserDTO);

        // Asserting the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuario cadastrado com username: validUsername", response.getBody());

        // Verifying service interaction
        verify(userService, times(1)).addUser(validUserDTO);
    }
}
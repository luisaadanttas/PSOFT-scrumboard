package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarUser(@RequestBody UserDTO userDTO) {
        String username;
        try {
            username = userService.addUser(userDTO);
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UserException e){
            return new ResponseEntity<String>("Username ja cadastrado", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>("Usuario cadastrado com username: " + username, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public ResponseEntity<?> editarUser(@RequestHeader String userLog,
    		@PathVariable String username, @RequestParam(required = false) String email,
    		@RequestParam(required = false) String nome) {
        try {
            userService.editUser(userLog, username, email, nome);
        } catch (UserException e) {
            return new ResponseEntity<String>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        } catch (OperationException e) {
            return new ResponseEntity<String>("Operação não permitida", HttpStatus.FORBIDDEN);
        }
        return  new ResponseEntity<String>("Usuario atualizado", HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletarUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        }catch (UserException e){
            return new ResponseEntity<String>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        } catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
        return new ResponseEntity<String>("Usuário removido", HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> consultarUser(@PathVariable String username){
        User user;
        try{
            user = userService.getUser(username);
        }catch (UserException e){
            return new ResponseEntity<String>("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


}

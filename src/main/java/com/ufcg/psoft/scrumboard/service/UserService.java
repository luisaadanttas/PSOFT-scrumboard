package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.model.Projeto;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.repository.ProjetoRepository;
import com.ufcg.psoft.scrumboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRep;

    @Autowired
    private ProjetoRepository projRep;

    public User getUser(String username) throws UserException{
        User user = this.userRep.getUser(username);
        if (user == null) throw new UserException("Usuario com username: " + username + " não encontrado");
        return user;
    }

    public String addUser(UserDTO userDTO) throws IllegalArgumentException, UserException {

        String username = userDTO.getUsername(), email = userDTO.getEmail(), nome = userDTO.getNome();
        if (username == null || username.isBlank() ||
            email == null || email.isBlank() ||
            nome == null || nome.isBlank())
            throw new IllegalArgumentException("Dados para cadastro inválidos. Algum campo está vazio.");

        User user = new User(email, nome, username);
        if (userRep.jaExiste(username)){
            throw new UserException("username já cadastrado");
        }
        this.userRep.addUser(user);
        return username;
    }

    public void editUser(String userLog, String username, String email, String nome) throws UserException, OperationException {
        if(!userLog.equals(username)) throw new OperationException("Operação não permitida");
        User user = getUser(username);
        if (email != null && !email.isBlank())
        	user.setEmail(email);
        if (nome != null && !nome.isBlank())
        	user.setNome(nome);
    }

    public void deleteUser(String username) throws UserException, OperationException{
        User user = userRep.getUser(username);
        if (user == null) throw new UserException("Usuario não encontrado");

        boolean isScrumMaster = false;
        for (Projeto p : projRep.getAll()) {
        	if (username.equals(p.getScrumMaster().getUsername())) {
        		isScrumMaster = true;
        		break;
        	}
        }
        if (isScrumMaster)
        	throw new OperationException("Usuário é scrum master de algum projeto. O scrum master não pode ser removido.");

        userRep.deleteUser(username);
        for (Projeto p : projRep.getAll())
        	p.removeMembro(username);
    }

}

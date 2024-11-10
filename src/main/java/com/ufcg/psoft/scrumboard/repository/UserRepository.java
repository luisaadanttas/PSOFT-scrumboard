package com.ufcg.psoft.scrumboard.repository;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.ufcg.psoft.scrumboard.model.User;

@Repository
public class UserRepository {
    private ArrayList<User> users;

    public UserRepository(){
        User user1 = new User("user@gmail.com", "joao", "joao");
        this.users = new ArrayList<User>();
        this.users.add(user1);
    }

    public String addUser(User user) {
        users.add(user);
        return (user.getUsername());
    }

    public User getUser(String username){
        for (User user : users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void editUser(User newUser){
        for (User user : users){
            if (user.getUsername().equals(newUser.getUsername())){
                user = newUser;
            }
        }
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }


    public boolean jaExiste(String username){
        for (User user : users){
            if (user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

}

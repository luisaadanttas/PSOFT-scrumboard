package com.ufcg.psoft.scrumboard.repository;

import com.ufcg.psoft.scrumboard.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private Map<String, User> users;

    public UserRepository(){
        this.users = new HashMap<String, User>();
    }

    public String addUser(User user) {
        this.users.put(user.getUsername(), user);
        return (user.getUsername());
    }

    public User getUser(String username){
        return this.users.get(username);
    }

    public void editUser(User user){
        this.users.replace(user.getUsername(), user);
    }

    public void deleteUser(String username){
        this.users.remove(username);
    }


    public boolean jaExiste(String username){
        return this.users.containsKey(username);
    }


}

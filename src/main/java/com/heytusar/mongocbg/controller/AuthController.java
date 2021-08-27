package com.heytusar.mongocbg.controller;

import java.util.List;

import com.heytusar.mongocbg.model.User;
import com.heytusar.mongocbg.service.AuthService;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@CrossOrigin
public class AuthController {
    private Logger log =  LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value="/users")
    List<User> getUsers() {
        return authService.getUsers();
    }

    @GetMapping(value="/user/{id}")
    public User getUser(@PathVariable String id) {
        return authService.getUser(id);
    }

    @PostMapping(value="/user/signin")
    public String signin(@RequestBody String jsonBody) {
        JSONObject json = new JSONObject(jsonBody);
        String usernameOrEmail = json.getString("email");
        String password = json.getString("password");
        JSONObject responseJson = authService.signin(usernameOrEmail, password);
        log.info("responseJson authCheck ----------------> " + responseJson);
        return responseJson.toString();
    }
    
    @PostMapping(value="/user")
    public String saveUser(@RequestBody String jsonBody) {
        JSONObject json = new JSONObject(jsonBody);
        String email = json.getString("email");
        String username = json.getString("username");
        String password = json.getString("password");
        JSONObject responseJson = authService.saveUser(email, username, password);
        log.info("responseJson saveUser ----------------> " + responseJson);
        return responseJson.toString();
    }
    
}

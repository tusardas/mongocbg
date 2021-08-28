package com.heytusar.mongocbg.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heytusar.mongocbg.model.User;
import com.heytusar.mongocbg.model.UserSession;
import com.heytusar.mongocbg.repository.UserRepository;
import com.heytusar.mongocbg.repository.UserSessionRepository;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class AuthService {
    private Logger log =  LoggerFactory.getLogger(AuthService.class);
    private UserRepository userRepository;
    private UserSessionRepository userSessionRepository;
    private JwtService jwtService;
    @Autowired
    public AuthService(UserRepository userRepository,
        UserSessionRepository userSessionRepository,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.jwtService = jwtService;
    }

    public Map signin(String usernameOrEmail, String password) {
        Map result = new HashMap<>();
        JSONObject json = new JSONObject();
        result.put("json", json);

        json.put("status", "failed");
        if(StringUtils.hasText(usernameOrEmail) == false
            || StringUtils.hasText(password)  == false) {
            json.put("error", "Please provide Username, Password");
            return result;
        }
        User user = userRepository.findByUsernameAndPassword(usernameOrEmail, password);
        if(user == null) {
            user = userRepository.findByEmailAndPassword(usernameOrEmail, password);
        }
        if(user == null) {
            json.put("error", "Incorrect Username/Email or Password");
            return result;
        }
        Map jwtParts = jwtService.makeJwt(user);
        /*
        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setLastHit(LocalDateTime.now());
        userSessionRepository.save(userSession);
        
        json.put("session", userSession.getId());
        */
        json.put("status", "ok");
        
        result.put("jwtParts", jwtParts);
        return result;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public JSONObject saveUser(String email, String username, String password) {
        JSONObject json = new JSONObject();
        json.put("status", "failed");
        if(StringUtils.hasText(email) == false
            || StringUtils.hasText(username)  == false
            || StringUtils.hasText(password)  == false) {

            json.put("error", "Please provide valid Email, Username, Password");
            return json;
        }

        User user = userRepository.findByEmail(email);
        if(user != null) {
            json.put("error", "Email is already registered");
            return json;
        }
        user = userRepository.findByUsername(username);
        if(user != null) {
            json.put("error", "Username is already registered");
            return json;
        }
        user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        json.put("status", "ok");
        json.put("user_id", user.getId());
        //throw new NullPointerException();
        
        return json;
    }

    public Map verifyAuth(String bearer) {
        Map result = jwtService.verifyAuth(bearer);
        return result;
    }
    
}

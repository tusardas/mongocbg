package com.heytusar.mongocbg.service;

import java.time.LocalDate;
import java.util.List;

import com.heytusar.mongocbg.model.User;
import com.heytusar.mongocbg.model.UserSession;
import com.heytusar.mongocbg.repository.UserRepository;
import com.heytusar.mongocbg.repository.UserSessionRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class AuthService {
    
    private UserRepository userRepository;
    private UserSessionRepository userSessionRepository;
    @Autowired
    public AuthService(UserRepository userRepository,
        UserSessionRepository userSessionRepository
    ) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    public JSONObject signin(String usernameOrEmail, String password) {
        JSONObject json = new JSONObject();
        json.put("status", "failed");
        if(StringUtils.hasText(usernameOrEmail) == false
            || StringUtils.hasText(password)  == false) {
            json.put("error", "Please provide Username, Password");
            return json;
        }
        User user = userRepository.findByUsernameAndPassword(usernameOrEmail, password);
        if(user == null) {
            user = userRepository.findByEmailAndPassword(usernameOrEmail, password);
        }
        if(user == null) {
            json.put("error", "Incorrect Username/Email or Password");
            return json;
        }
        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setLastHit(LocalDate.now());
        userSessionRepository.save(userSession);
        json.put("status", "ok");
        json.put("session", userSession.getId());
        return json;
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
}

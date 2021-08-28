package com.heytusar.mongocbg.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.heytusar.mongocbg.model.User;
import com.heytusar.mongocbg.service.AuthService;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class AuthController {
    private Logger log =  LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value="/users")
    List<User> getUsers() {
        log.info("Returning many users from controller --------------->");
        return authService.getUsers();
    }

    @GetMapping(value="/user/{id}")
    public User getUser(@PathVariable String id) {
        log.info("Returning single user from controller --------------->");
        return authService.getUser(id);
    }

    @PostMapping(value="/user/signin")
    public ResponseEntity<String> signin(@RequestBody String jsonBody, HttpServletResponse response) {
        JSONObject json = new JSONObject(jsonBody);
        String usernameOrEmail = json.getString("email");
        String password = json.getString("password");
        JSONObject responseJson = authService.signin(usernameOrEmail, password);
        log.info("responseJson authCheck ----------------> " + responseJson);
        HttpHeaders headers = new HttpHeaders();
        if(responseJson.get("status") == "ok") {
            ResponseCookie cookie1 = ResponseCookie.from("cookie1", "cookie1val")
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();
            ResponseCookie cookie2 = ResponseCookie.from("cookie2", "cookie2val")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();
            headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
            headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        }
        log.info("Returning from controller --------------->" + responseJson.toString());
        return new ResponseEntity<String>(responseJson.toString(), headers, HttpStatus.OK);
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

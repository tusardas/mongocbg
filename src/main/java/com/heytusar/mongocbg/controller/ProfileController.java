package com.heytusar.mongocbg.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    @GetMapping(value="/api/profile")
    public ResponseEntity<String> getProfile(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", "ok");
        responseJson.put("userId", userId);
        return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
    }
}

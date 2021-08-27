package com.heytusar.mongocbg.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserSession {
    @Id
    private String id;
    private String userId;
    private LocalDate lastHit;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public LocalDate getLastHit() {
        return lastHit;
    }
    public void setLastHit(LocalDate lastHit) {
        this.lastHit = lastHit;
    }
}

package com.heytusar.mongocbg.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserSession {
    @Id
    private String id;
    private String userId;
    private LocalDateTime lastHit;
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
    public LocalDateTime getLastHit() {
        return lastHit;
    }
    public void setLastHit(LocalDateTime lastHit) {
        this.lastHit = lastHit;
    }
}

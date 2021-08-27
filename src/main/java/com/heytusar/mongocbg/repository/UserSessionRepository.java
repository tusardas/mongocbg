package com.heytusar.mongocbg.repository;

import com.heytusar.mongocbg.model.UserSession;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSession, String> {
    
}

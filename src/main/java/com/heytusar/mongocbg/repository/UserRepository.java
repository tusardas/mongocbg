package com.heytusar.mongocbg.repository;

import com.heytusar.mongocbg.model.CbgUser;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<CbgUser, String> {
    CbgUser findByUsernameAndPassword(String username, String password);
    CbgUser findByEmailAndPassword(String email, String Password);
    CbgUser findByEmail(String email);
    CbgUser findByUsername(String username);
}

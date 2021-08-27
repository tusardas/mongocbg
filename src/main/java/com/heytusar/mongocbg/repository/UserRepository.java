package com.heytusar.mongocbg.repository;

import com.heytusar.mongocbg.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsernameAndPassword(String username, String password);
    User findByEmailAndPassword(String email, String Password);
    User findByEmail(String email);
    User findByUsername(String username);
}

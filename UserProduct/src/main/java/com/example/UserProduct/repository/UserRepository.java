package com.example.UserProduct.repository;

import com.example.UserProduct.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
        User findByUserId(String userId);
}

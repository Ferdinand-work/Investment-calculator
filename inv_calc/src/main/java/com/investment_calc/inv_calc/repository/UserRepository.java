package com.investment_calc.inv_calc.repository;

import com.investment_calc.inv_calc.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User,String> {
    public User findByEmail(String email);
}

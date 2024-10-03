package com.investment_calc.inv_calc.repository;

import com.investment_calc.inv_calc.model.Investment;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Repository
public interface CalcRepository extends MongoRepository<Investment,String>{
    List<Investment> findByTypeAndEmail(String type, String email);
}

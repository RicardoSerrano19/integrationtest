package com.rsr.integrationtest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rsr.integrationtest.domain.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>{
    
}

package com.rsr.integrationtest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rsr.integrationtest.domain.Employee;
import com.rsr.integrationtest.exception.DocumentNotFoundException;
import com.rsr.integrationtest.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Employee employeeSaved = employeeRepository.save(employee);
        return employeeSaved;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        Optional<Employee> eOptional = employeeRepository.findById(id);
        if(eOptional.isPresent()) return eOptional;
        throw new DocumentNotFoundException("Document with that id do not exist");
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteEmployee(String id) {
        // TODO Auto-generated method stub
        
    }
    
}

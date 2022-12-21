package com.rsr.integrationtest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rsr.integrationtest.domain.Employee;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        // TODO Auto-generated method stub
        return Optional.empty();
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

package com.rsr.integrationtest.service;

import java.util.List;
import java.util.Optional;

import com.rsr.integrationtest.domain.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(String id);
    Employee updateEmployee(String id, Employee updatedEmployee);
    void deleteEmployee(String id);
}

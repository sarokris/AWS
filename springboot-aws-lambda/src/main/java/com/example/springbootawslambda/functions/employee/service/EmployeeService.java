package com.example.springbootawslambda.functions.employee.service;

import com.example.springbootawslambda.functions.employee.dto.Employee;

import java.util.List;

public interface EmployeeService {

	List<Employee> findAll();

	Employee createEmployee(Employee employee);

}

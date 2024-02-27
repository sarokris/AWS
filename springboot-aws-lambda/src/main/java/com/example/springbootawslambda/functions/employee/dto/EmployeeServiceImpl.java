package com.example.springbootawslambda.functions.employee.dto;

import com.example.springbootawslambda.functions.employee.service.EmployeeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Override
	public List<Employee> findAll() {
		// TODO read from dynamo and handle error scenario
		throw new UnsupportedOperationException("not implemented");
	}

}

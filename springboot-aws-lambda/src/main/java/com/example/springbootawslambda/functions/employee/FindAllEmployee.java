package com.example.springbootawslambda.functions.employee;

import com.example.springbootawslambda.functions.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllEmployee {

	private final EmployeeService employeeService;

}

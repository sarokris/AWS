package com.example.springbootawslambda.functions.employee.service;

import com.example.springbootawslambda.functions.employee.dto.Employee;
import com.example.springbootawslambda.functions.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.springbootawslambda.config.AppConstants.EMPLOYEE_TABLE;

@Component("employeeService")
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final DynamoDbClient dynamoDbClient;

	@Override
	public List<Employee> findAll() {
		// TODO read from dynamo and handle error scenario
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public Employee createEmployee(Employee employee) {
		// TODO update the logic once the db is set up
		Map<String, AttributeValue> itemValues = new HashMap<>();
		itemValues.put("id", AttributeValue.builder().s(employee.empId()).build());
		itemValues.put("name", AttributeValue.builder().s(employee.name()).build());
		itemValues.put("department", AttributeValue.builder().s(employee.department()).build());

		PutItemRequest request = PutItemRequest.builder().tableName(EMPLOYEE_TABLE).item(itemValues).build();

		try {
			PutItemResponse response = dynamoDbClient.putItem(request);
			log.info("Employee created successfully");
		}
		catch (DynamoDbException e) {
			throw new RuntimeException(e.getMessage());
		}
		return employee;
	}

}

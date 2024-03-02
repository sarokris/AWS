package com.example.springbootawslambda.functions.employee.service;

import com.example.springbootawslambda.functions.employee.dto.Employee;
import com.example.springbootawslambda.functions.employee.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.springbootawslambda.config.AppConstants.EMPLOYEE_TABLE;
import static com.example.springbootawslambda.config.AppConstants.EMP_TAB_PARTITION_KEY;

@Component("employeeService")
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final DynamoDbClient dynamoDbClient;

	@Override
	public List<Employee> findAll() {
		// Create a ScanRequest to scan the entire table
		ScanRequest scanRequest = ScanRequest.builder().tableName(EMPLOYEE_TABLE).build();

		// Perform the scan and retrieve all items from the table
		ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
		List<Map<String, AttributeValue>> items = scanResponse.items();

		// Process and print the retrieved items
		EmployeeMapper employeeMapper = new EmployeeMapper();
		return items.stream().map(employeeMapper).toList();
	}

	@Override
	public Employee findById(String id) {
		// Create a QueryRequest to query items based on the partition key
		QueryRequest queryRequest = QueryRequest.builder()
			.tableName(EMPLOYEE_TABLE)
			.keyConditionExpression(EMP_TAB_PARTITION_KEY + " = :val")
			.expressionAttributeValues(Map.of(":val", AttributeValue.builder().s(id).build()))
			.build();

		// Perform the query and retrieve matching items from the table
		QueryResponse queryResponse = dynamoDbClient.query(queryRequest);
		if (!queryResponse.hasItems()) {
			throw new RuntimeException("Employee not found with ID:" + id);
		}
		EmployeeMapper employeeMapper = new EmployeeMapper();
		List<Employee> employeeList = queryResponse.items().stream().map(employeeMapper).toList();
		return employeeList.get(0);
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

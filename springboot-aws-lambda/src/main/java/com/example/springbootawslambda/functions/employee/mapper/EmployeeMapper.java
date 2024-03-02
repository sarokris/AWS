package com.example.springbootawslambda.functions.employee.mapper;

import com.example.springbootawslambda.functions.employee.dto.Employee;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.function.Function;

public class EmployeeMapper implements Function<Map<String, AttributeValue>, Employee> {

	@Override
	public Employee apply(Map<String, AttributeValue> attributeValueMap) {
		String id = null, name = null, dep = null;
		if (attributeValueMap.get("id") != null) {
			id = attributeValueMap.get("id").s();
		}
		if (attributeValueMap.get("name") != null) {
			name = attributeValueMap.get("name").s();
		}
		if (attributeValueMap.get("department") != null) {
			dep = attributeValueMap.get("department").s();
		}
		return new Employee(id, name, dep);
	}

}

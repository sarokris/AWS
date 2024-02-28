package com.example.springbootawslambda.functions.employee;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.example.springbootawslambda.SpringbootAwsLambdaApplication;
import com.example.springbootawslambda.functions.employee.dto.Employee;
import com.example.springbootawslambda.functions.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CreateEmployeeTest {

    private final EmployeeService employeeService;

    private final ObjectMapper objectMapper;

    @Test
    public void testCreateEmployee() throws Exception {
        System.setProperty("MAIN_CLASS", SpringbootAwsLambdaApplication.class.getName());
        System.setProperty("spring.cloud.function.definition", "createEmployee");

        Employee employee = new Employee("101","Jhon","IT");

        APIGatewayV2HTTPEvent createEmpEvent = APIGatewayV2HTTPEvent.builder()
                .withHeaders(Map.of("Content-Type","application/json"))
                .withBody(objectMapper.writeValueAsString(employee))
                .build();

        FunctionInvoker invoker = new FunctionInvoker();

        InputStream targetStream = new ByteArrayInputStream(objectMapper.writeValueAsString(createEmpEvent).getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        invoker.handleRequest(targetStream, output, null);

        String responseBody = new String(output.toByteArray(), StandardCharsets.UTF_8);
		String result = new ObjectMapper().readTree(responseBody).get("body").textValue();
        Employee expectedEmployee = objectMapper.readValue(result,Employee.class);
        Assertions.assertEquals(expectedEmployee, employee);
    }



}

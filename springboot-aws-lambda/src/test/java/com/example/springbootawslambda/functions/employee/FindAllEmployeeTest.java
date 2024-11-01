package com.example.springbootawslambda.functions.employee;

import cloud.localstack.ServiceName;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.example.springbootawslambda.SpringbootAwsLambdaApplication;
import com.example.springbootawslambda.functions.employee.dto.Employee;
import com.example.springbootawslambda.util.DynamoDBUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.example.springbootawslambda.config.AppConstants.STATUS_CODE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { ServiceName.DYNAMO })
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FindAllEmployeeTest {

	private final ObjectMapper objectMapper;

	@BeforeAll
	public static void setUp() {
		DynamoDBUtils.loadData();
	}

	@Test
	public void testFindAllEmployee() throws Exception {
		System.setProperty("MAIN_CLASS", SpringbootAwsLambdaApplication.class.getName());
		System.setProperty("spring.cloud.function.definition", "findAllEmployee");

		APIGatewayV2HTTPEvent createEmpEvent = APIGatewayV2HTTPEvent.builder()
			.withHeaders(Map.of("Content-Type", "application/json"))
			.withBody("")
			.build();

		FunctionInvoker invoker = new FunctionInvoker();

		InputStream targetStream = new ByteArrayInputStream(objectMapper.writeValueAsString(createEmpEvent).getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		invoker.handleRequest(targetStream, output, null);

		String responseBody = new String(output.toByteArray(), StandardCharsets.UTF_8);
		JsonNode response = objectMapper.readTree(responseBody);
		String resultSet = response.get("body").textValue();
		List<Employee> empList = objectMapper.readValue(resultSet, new TypeReference<>() {
		});
		Assertions.assertEquals(HttpStatus.OK.value(), response.get(STATUS_CODE).intValue());
		Assertions.assertFalse(CollectionUtils.isEmpty(empList));

	}

}
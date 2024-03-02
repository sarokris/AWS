package com.example.springbootawslambda.functions.employee;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.example.springbootawslambda.functions.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllEmployee implements Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	private final EmployeeService employeeService;

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public APIGatewayV2HTTPResponse apply(APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent) {
		return APIGatewayV2HTTPResponse.builder()
			.withStatusCode(HttpStatus.OK.value())
			.withBody(objectMapper.writeValueAsString(employeeService.findAll()))
			.build();
	}

}

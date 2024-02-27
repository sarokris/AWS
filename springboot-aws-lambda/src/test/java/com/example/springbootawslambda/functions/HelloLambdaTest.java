package com.example.springbootawslambda.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.example.springbootawslambda.SpringbootAwsLambdaApplication;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloLambdaTest {

	@Test
	public void testHelloLambda() throws Exception {
		System.setProperty("MAIN_CLASS", SpringbootAwsLambdaApplication.class.getName());
		System.setProperty("spring.cloud.function.definition", "helloLambda");

		FunctionInvoker invoker = new FunctionInvoker();

		InputStream targetStream = new ByteArrayInputStream("Test".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		invoker.handleRequest(targetStream, output, null);

		String result = new String(output.toByteArray(), StandardCharsets.UTF_8);
		Assertions.assertEquals(result, "TEST");

	}

}

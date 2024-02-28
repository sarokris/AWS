package com.example.springbootawslambda.functions;

import com.example.springbootawslambda.SpringbootAwsLambdaApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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

		String body = """
					{ "body" : "HelloLambda"}
				""";

		FunctionInvoker invoker = new FunctionInvoker();

		InputStream targetStream = new ByteArrayInputStream("HelloLambda".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		invoker.handleRequest(targetStream, output, null);

		String responseBody = new String(output.toByteArray(), StandardCharsets.UTF_8);
//		String result = new ObjectMapper().readTree(responseBody).get("body").textValue();
		Assertions.assertEquals(responseBody, "HELLOLAMBDA");

	}

}

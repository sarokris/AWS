package com.example.springbootawslambda.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.example.springbootawslambda.SpringbootAwsLambdaApplication;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor
public class HelloLambdaTest {

    private TestRestTemplate testRestTemplate;

    @Test
    public void testHelloLambda() throws Exception{
        System.setProperty ("MAIN_CLASS", SpringbootAwsLambdaApplication.class.getName());
        System. setProperty("spring.cloud.function.definition", "helloLambda");

    }
}

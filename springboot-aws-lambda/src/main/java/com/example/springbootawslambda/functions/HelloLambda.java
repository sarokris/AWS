package com.example.springbootawslambda.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.function.Function;

@Slf4j
@Component
public class HelloLambda implements Function<String, String> {
    @Override
    public String apply(String input) {
//        return APIGatewayV2HTTPResponse.builder().withStatusCode(HttpStatus.OK.value()).withBody("Hello Lambda").build();
        return input.toUpperCase();
    }
}

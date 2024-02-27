package com.example.springbootawslambda.functions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
public class HelloLambda implements Function<String, String> {

	@Override
	public String apply(String input) {
		return input.toUpperCase();
	}

}

package com.example.springbootawslambda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class AwsServicesConfig {

	@Value("${aws.endpoint}")
	private String endpoint;

	@Value("${aws.region}")
	private String region;

	// @Bean
	// public DynamoDbClient dynamoDbClient() {
	// return DynamoDbClient.builder().region(Region.US_EAST_1).build();
	// }

	// uncomment the below code while doing unit testing
	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder().endpointOverride(URI.create(endpoint)).region(Region.of(region)).build();
	}

}

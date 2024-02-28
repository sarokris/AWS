package com.example.springbootawslambda.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class AwsServicesTestConfig {

	@Value("${aws.access.key.id}")
	private String accessKey;

	@Value("${aws.secret.access.key}")
	private String secretAccessKey;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.endpoint}")
	private String endpoint;

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder().endpointOverride(URI.create(endpoint)).region(Region.of(region)).build();
	}



}

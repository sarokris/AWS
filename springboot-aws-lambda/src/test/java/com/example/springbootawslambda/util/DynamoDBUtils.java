package com.example.springbootawslambda.util;

import cloud.localstack.Localstack;
import com.example.springbootawslambda.config.AppConstants;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.net.URI;

import static com.example.springbootawslambda.config.AppConstants.EMPLOYEE_TABLE;
import static com.example.springbootawslambda.config.AppConstants.EMP_TAB_PARTITION_KEY;

public class DynamoDBUtils {

	public static void createTable() {
		try (DynamoDbClient ddbClient = DynamoDbClient.builder()
			.endpointOverride(URI.create("http://127.0.0.1:4566"))
			.region(Region.US_EAST_1)
			.build()) {

			CreateTableRequest createTableRequest = CreateTableRequest.builder()
				.attributeDefinitions(AttributeDefinition.builder()
					.attributeName(EMP_TAB_PARTITION_KEY)
					.attributeType(ScalarAttributeType.S)
					.build())
				.keySchema(
						KeySchemaElement.builder().attributeName(EMP_TAB_PARTITION_KEY).keyType(KeyType.HASH).build())
				.provisionedThroughput(
						ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
				.tableName(EMPLOYEE_TABLE)
				.build();
			CreateTableResponse response = ddbClient.createTable(createTableRequest);
			System.out.println("Employee table created successfully");

		}
	}

}

package com.example.springbootawslambda.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static void loadData() {
		createTable();
		// Specify the updates you want to perform.
		List<WriteRequest> writeRequests = new ArrayList<>();

		try (DynamoDbClient ddbClient = DynamoDbClient.builder()
			.endpointOverride(URI.create("http://127.0.0.1:4566"))
			.region(Region.US_EAST_1)
			.build()) {
			// Create items to be inserted

			Map<String, AttributeValue> item1 = new HashMap<>();
			item1.put("id", AttributeValue.builder().s("101").build());
			item1.put("name", AttributeValue.builder().s("john").build());
			item1.put("department", AttributeValue.builder().s("IT").build());
			writeRequests.add(WriteRequest.builder().putRequest(PutRequest.builder().item(item1).build()).build());

			Map<String, AttributeValue> item2 = new HashMap<>();
			item2.put("id", AttributeValue.builder().s("102").build());
			item2.put("name", AttributeValue.builder().s("Sam").build());
			item2.put("department", AttributeValue.builder().s("HR").build());
			writeRequests.add(WriteRequest.builder().putRequest(PutRequest.builder().item(item2).build()).build());

			// Create a BatchWriteItemRequest
			BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
				.requestItems(Map.of(EMPLOYEE_TABLE, writeRequests))
				.build();

			// Perform the batch write operation
			BatchWriteItemResponse batchWriteItemResponse = ddbClient.batchWriteItem(batchWriteItemRequest);

			// Process the response
			Map<String, List<WriteRequest>> unprocessedItems = batchWriteItemResponse.unprocessedItems();
			if (!unprocessedItems.isEmpty()) {
				System.out.println("Some items were not processed due to exceeding throughput limits.");
			}
			else {
				System.out.println("All items were processed successfully.");
			}

		}
	}

}

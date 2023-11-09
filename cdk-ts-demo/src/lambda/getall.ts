import * as AWS from 'aws-sdk';
import {  DynamoDBClient } from "@aws-sdk/client-dynamodb";
import {
  DynamoDBDocumentClient,
  ScanCommand
} from "@aws-sdk/lib-dynamodb";


const client = new DynamoDBClient({
  region:process.env.CDK_DEFAULT_REGION,
  endpoint: 'http://localhost.localstack.cloud:4566'
});
const dynamo = DynamoDBDocumentClient.from(client);
const TABLE_NAME = process.env.TABLE_NAME || '';


export const handler = async (event: any) => {

    try {
        console.log('connecting to dyname db: ' +  TABLE_NAME)

        var body = await dynamo.send(
          new ScanCommand({ TableName: TABLE_NAME })
        );
      
        const response = body?.Items;
        const formattedRes = response?JSON.stringify(response):'No value present';
        return { statusCode: 200, body: formattedRes };
    } catch (dbError) {  
        console.log('connecting to dyname db: ' +  TABLE_NAME)      
        return { statusCode: 500, body: JSON.stringify(dbError) };
    }
  };
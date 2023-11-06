// import * as AWS from 'aws-sdk';
import {  DynamoDB, DynamoDBClient } from "@aws-sdk/client-dynamodb";
import {
  DynamoDBDocumentClient,
  ScanCommand
} from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({
    region: process.env.CDK_DEFAULT_REGION,
    endpoint: 'http://localhost:4566',


});

// const db = new DynamoDB({
//     region: process.env.CDK_DEFAULT_REGION,
//     endpoint: 'http://localhost:4566',
//     accessKeyId: 'test',
//     secretAccessKey: 'test'
//   });

const dynamo = DynamoDBDocumentClient.from(client);
const TABLE_NAME = process.env.TABLE_NAME || '';


export const handler = async (event: any) => {

    try {
        console.log('connecting to dyname db: ' +  TABLE_NAME)
        var body = await dynamo.send(
            new ScanCommand({ TableName: TABLE_NAME })
          );

        //   const params = {
        //     TableName: TABLE_NAME
        //   };

        //   const response = await db.scan(params).promise();
          
        const response = body?.Items;
        const formattedRes = response?JSON.stringify(response):'No value present';
        return { statusCode: 200, body: formattedRes };
    } catch (dbError) {  
        console.log('connecting to dyname db: ' +  TABLE_NAME)      
        return { statusCode: 500, body: JSON.stringify(dbError) };
    }
  };
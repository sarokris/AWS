import {  DynamoDBClient } from "@aws-sdk/client-dynamodb";
import {
  DynamoDBDocumentClient,
  PutCommand
} from "@aws-sdk/lib-dynamodb";


const client = new DynamoDBClient({
  region:process.env.CDK_DEFAULT_REGION,
  endpoint: 'http://localhost.localstack.cloud:4566'
});

const dynamo = DynamoDBDocumentClient.from(client);
const TABLE_NAME = process.env.TABLE_NAME || '';
const PRIMARY_KEY = process.env.PRIMARY_KEY || '';

export const handler = async (event: any = {}): Promise<any> => {

  if (!event.body) {
    return { statusCode: 400, body: 'invalid request, you are missing the parameter body' };
  }
  const item = typeof event.body == 'object' ? event.body : JSON.parse(event.body);
  // item[PRIMARY_KEY] = uuidv4();
  const params = {
    TableName: TABLE_NAME,
    Item: item
  };

  try {
    await dynamo.send(new PutCommand(params));
    return { statusCode: 201, body: '' };
  } catch (dbError: any) {
     const errorResponse = 'Exception while persisting the demo Item ' + dbError;
    return { statusCode: 500, body: errorResponse };
  }
};



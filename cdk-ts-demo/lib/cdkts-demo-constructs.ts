import * as iam from 'aws-cdk-lib/aws-iam';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb'
import * as apigateway from 'aws-cdk-lib/aws-apigateway';
import { Duration } from "aws-cdk-lib";
import { Construct } from "constructs"
import { DemoApiProps, LambdaListType } from "./cdk-ts-demo-prop";
import { Code, Runtime } from 'aws-cdk-lib/aws-lambda';


export class DemoApiConstructs  extends Construct {
    constructor(scope: Construct, id: string,prop: DemoApiProps) {
        super(scope,id);

        const lambdaList:LambdaListType[] =  prop.lambdaFunctions;

        const dynamodbTable = new dynamodb.Table(this,'demo-table',{
            partitionKey : {
                name: 'demoId',
                type: dynamodb.AttributeType.STRING

            }
        });

        const api = new apigateway.RestApi(this,'demoApi');
        const resource = api.root.addResource('demo');

        lambdaList.map(value => {
            const lambdaFunction = this.createLambdaFunction(
                value.fuctionId,
                value.fuctionName,
                value.handler,
                value.timeOut ? value.timeOut : Duration.seconds(3),
                dynamodbTable.tableName,
                'demoId'
                );

                const integration = new apigateway.LambdaIntegration(lambdaFunction); 
                if(value.path!= null){
                    const pathPartResource = resource.addResource(value.path);
                    pathPartResource.addMethod(value.method, integration);
                }else{
                    resource.addMethod(value.method, integration);    
                } 

                dynamodbTable.grantReadWriteData(lambdaFunction);

        });
    }


    createLambdaFunction(id: string, name: string, handlerPath: string,timeOut: Duration
        ,tableName: string,pk: string, role?: iam.IRole)  {
        return new lambda.Function(this, id , {
            functionName: name,
            runtime: Runtime.NODEJS_18_X,
            code: Code.fromAsset('lambda'),
            handler: handlerPath,
            timeout: timeOut,
            environment:{
                TABLE_NAME: tableName,
                PRIMARY_KEY: pk
            }
        });
    }
    
}
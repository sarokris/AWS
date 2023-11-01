import * as iam from 'aws-cdk-lib/aws-iam';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigateway from 'aws-cdk-lib/aws-apigateway';
import { Duration } from "aws-cdk-lib";
import { Construct } from "constructs"
import { DemoApiProps, LambdaListType } from "./cdk-ts-demo-prop";
import { Code, Runtime } from 'aws-cdk-lib/aws-lambda';


export class DemoApiConstructs  extends Construct {
    constructor(scope: Construct, id: string,prop: DemoApiProps) {
        super(scope,id);

        const lambdaList:LambdaListType[] =  prop.lambdaFunctions;

        const api = new apigateway.RestApi(this,'demoApi');

        lambdaList.map(value => {
            const lambdaFunction = this.createLambdaFunction(
                value.fuctionId,
                value.fuctionName,
                value.handler,
                value.timeOut ? value.timeOut : Duration.seconds(3)
                );

                const integration = new apigateway.LambdaIntegration(lambdaFunction);

                const resource = api.root.addResource('demo');
                resource.addMethod(value.method, integration);

        });
    }


    createLambdaFunction(id: string, name: string, handlerPath: string,timeOut: Duration, role?: iam.IRole)  {
        return new lambda.Function(this, id , {
            functionName: name,
            runtime: Runtime.NODEJS_18_X,
            code: Code.fromAsset('lambda'),
            handler: handlerPath
        });
    }
    
}
import * as path from 'path';
import * as iam from 'aws-cdk-lib/aws-iam';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import { Duration } from "aws-cdk-lib";
import { Construct } from "constructs"
import { DemoApiProps, LambdaListType } from "./cdk-ts-demo-prop";
import { Code, Runtime } from 'aws-cdk-lib/aws-lambda';

export class DemoApiConstructs  extends Construct {
    constructor(scope: Construct, id: string,prop: DemoApiProps) {
        super(scope,id);

        const lambdaList:LambdaListType[] =  prop.lambdaFunctions;

        lambdaList.map(value => {
            const lambdaFunction = this.createLambdaFunction(
                value.fuctionId,
                value.fuctionName,
                value.handler,
                value.timeOut ? value.timeOut : Duration.seconds(3)
                );

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
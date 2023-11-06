import * as iam from 'aws-cdk-lib/aws-iam';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigateway from 'aws-cdk-lib/aws-apigateway';
import { Duration } from "aws-cdk-lib";
import { Construct } from "constructs"
import { Code, Runtime } from 'aws-cdk-lib/aws-lambda';
import { HttpMethod } from 'aws-cdk-lib/aws-events';

export interface DemoApiProps {
    lambdaFunctions: LambdaListType[]
}

export type LambdaListType = {
    fuctionName: string,
    fuctionId: string,
    handler: string,
    path?: string,
    method: HttpMethod,
    timeOut: Duration     
}


export class DemoApiConstructs  extends Construct {
    constructor(scope: Construct, id: string,prop: DemoApiProps) {
        super(scope,id);

        const lambdaList:LambdaListType[] =  prop.lambdaFunctions;

        const api = new apigateway.RestApi(this,'demoApi');
        const resource = api.root.addResource('demo');

        lambdaList.map(value => {
            const lambdaFunction = this.createLambdaFunction(
                value.fuctionId,
                value.fuctionName,
                value.handler,
                value.timeOut ? value.timeOut : Duration.seconds(3)
                );

                const integration = new apigateway.LambdaIntegration(lambdaFunction); 
                if(value.path!= null){
                    const pathPartResource = resource.addResource(value.path);
                    pathPartResource.addMethod(value.method, integration);
                }else{
                    resource.addMethod(value.method, integration);    
                } 
                
        });
    }


    createLambdaFunction(id: string, name: string, handlerPath: string,timeOut: Duration, role?: iam.IRole)  {
        return new lambda.Function(this, id , {
            functionName: name,
            runtime: Runtime.NODEJS_18_X,
            code: Code.fromAsset('src/lambda'),
            handler: handlerPath
        });
    }
    
}
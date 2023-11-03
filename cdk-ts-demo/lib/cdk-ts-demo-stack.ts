import * as cdk from 'aws-cdk-lib';
import { Duration } from 'aws-cdk-lib';
import { HttpMethod } from 'aws-cdk-lib/aws-events';
import { Construct } from 'constructs';
import path = require('path');
import { DemoApiConstructs } from './cdkts-demo-constructs';

export class CdkTsDemoStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    new DemoApiConstructs(this, 'DemoApiConstructs', {
      lambdaFunctions: [
        {
          fuctionName: 'demoHealthApi',
          fuctionId: 'demoHealthApi',
          handler: 'health.handler',
          path: 'health',
          method: HttpMethod.GET,
          timeOut: Duration.seconds(3)
        },
        {
          fuctionName: 'demoPostApi',
          fuctionId: 'demoPostApi',
          handler: 'create-demo.handler',
          method: HttpMethod.POST,
          timeOut: Duration.seconds(3)
        },
        {
          fuctionName: 'demoGetAllApi',
          fuctionId: 'demoGetAllApi',
          handler: 'get-all.handler',
          method: HttpMethod.GET,
          timeOut: Duration.seconds(3)
        }
        // ,
        // {
        //   fuctionName: 'demoGetByIdApi',
        //   fuctionId: 'demoGetByIdApi',
        //   handler: 'health.handler',
        //   path: '/api/health',
        //   method: HttpMethod.GET,
        //   timeOut: Duration.seconds(3)
        // }
      ]
    });


  }
}

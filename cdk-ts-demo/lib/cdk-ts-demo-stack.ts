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
          fuctionName: 'demoApi',
          fuctionId: 'demoApiV1',
          handler: 'health.handler',
          path: 'health',
          method: HttpMethod.GET,
          timeOut: Duration.seconds(3)
        },
        {
          fuctionName: 'demoGetAllApi',
          fuctionId: 'demoGetAllApiV1',
          handler: 'getall.handler',
          method: HttpMethod.GET,
          timeOut: Duration.seconds(3)
        }
      ]
    });


  }
}

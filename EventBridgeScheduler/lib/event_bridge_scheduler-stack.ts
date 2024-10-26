import * as cdk from 'aws-cdk-lib';
import * as lambda from 'aws-cdk-lib/aws-lambda'
import * as events from 'aws-cdk-lib/aws-events'
import * as targets from 'aws-cdk-lib/aws-events-targets'
import { Duration } from 'aws-cdk-lib'; 
import { Construct } from 'constructs';

export class EventBridgeSchedulerStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // Create Lambda function
    const myLambda = new lambda.Function(this, 'MyLambdaFunction', {
      runtime: lambda.Runtime.NODEJS_16_X, // Define the runtime environment
      handler: 'index.handler', // Define the handler method
      code: lambda.Code.fromInline(`
        exports.handler = async function(event) {
          console.log("Event triggered by EventBridge One minute:", event);
           return {
              statusCode: 200,
              body: JSON.stringify({ message: "Hello from Lambda!" }),
            };
        };
      `), // Inline code for demonstration; use lambda.Code.fromAsset('path/to/code') for external code
    });

    // Create EventBridge rule to trigger Lambda every minute
    const rule = new events.Rule(this, 'EveryOneMinuteRule', {
      schedule: events.Schedule.rate(Duration.minutes(1)), 
    });

    // Add Lambda as target for the EventBridge rule
    rule.addTarget(new targets.LambdaFunction(myLambda));


  }
}

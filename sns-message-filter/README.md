# Welcome to your CDK TypeScript project

This is a blank project for CDK development with TypeScript.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

## Useful commands

* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk synth`       emits the synthesized CloudFormation template

Order Processing System
Scenario: An e-commerce platform where orders need to be processed differently based on order status (new, processing, completed, canceled).

Implementation:

Order Events: Triggered by updates to an order status in a database or event stream.
SNS Topic: Publish order events to an SNS topic with metadata (e.g., orderStatus).
SQS Queues: Subscribe different SQS queues to the SNS topic with filter policies based on orderStatus.
Consumers: Develop Lambda functions or order processing services to handle order fulfillment, inventory management, customer notifications, etc.
Benefits:

Order Management: Streamline order processing workflows based on real-time status updates.
Flexibility: Handle different order statuses and processing steps independently.
Reliability: Ensure that critical order-related tasks are isolated and managed independently.
Conclusion
These use cases demonstrate how using fanout with message filtering via SNS and SQS can effectively decouple components, enable efficient message processing based on attributes, and scale applications while maintaining flexibility and reliability. By leveraging AWS services in this manner, you can design robust and scalable architectures that meet specific business needs efficiently.


sample code needs to be removed 

import * as cdk from '@aws-cdk/core';
import * as sns from '@aws-cdk/aws-sns';
import * as sqs from '@aws-cdk/aws-sqs';
import * as lambda from '@aws-cdk/aws-lambda';
import * as iam from '@aws-cdk/aws-iam';
import * as lambdaEventSources from '@aws-cdk/aws-lambda-event-sources';
import * as s3 from '@aws-cdk/aws-s3';
import * as s3deploy from '@aws-cdk/aws-s3-deployment';
import * as path from 'path';

export class MyCdkProjectStack extends cdk.Stack {
  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // Create SNS Topic
    const mySNSTopic = new sns.Topic(this, 'MySNSTopic', {
      topicName: 'MySNSTopic',
    });

    // Create SQS Queues
    const queueA = new sqs.Queue(this, 'QueueA', {
      queueName: 'QueueA',
    });

    const queueB = new sqs.Queue(this, 'QueueB', {
      queueName: 'QueueB',
    });

    // Create SNS Subscriptions with filter policies
    new sns.Subscription(this, 'QueueASubscription', {
      endpoint: queueA.queueArn,
      protocol: sns.SubscriptionProtocol.SQS,
      topic: mySNSTopic,
      filterPolicy: {
        type: sns.SubscriptionFilter.stringFilter({
          whitelist: ['typeA'],
        }),
      },
    });

    new sns.Subscription(this, 'QueueBSubscription', {
      endpoint: queueB.queueArn,
      protocol: sns.SubscriptionProtocol.SQS,
      topic: mySNSTopic,
      filterPolicy: {
        type: sns.SubscriptionFilter.stringFilter({
          whitelist: ['typeB'],
        }),
      },
    });

    // Create IAM Role for Lambda to poll SQS
    const lambdaRole = new iam.Role(this, 'LambdaSQSPollRole', {
      assumedBy: new iam.ServicePrincipal('lambda.amazonaws.com'),
      managedPolicies: [
        iam.ManagedPolicy.fromAwsManagedPolicyName('service-role/AWSLambdaBasicExecutionRole'),
      ],
    });

    lambdaRole.addToPolicy(new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'sqs:ReceiveMessage',
        'sqs:DeleteMessage',
        'sqs:GetQueueAttributes',
      ],
      resources: [
        queueA.queueArn,
        queueB.queueArn,
      ],
    }));

    // Create Lambda Functions
    const myLambdaA = new lambda.Function(this, 'MyLambdaA', {
      runtime: lambda.Runtime.NODEJS_14_X,
      handler: 'index.handler',
      code: lambda.Code.fromAsset('lambda'),
      environment: {
        QUEUE_URL: queueA.queueUrl,
      },
      role: lambdaRole,
    });

    const myLambdaB = new lambda.Function(this, 'MyLambdaB', {
      runtime: lambda.Runtime.NODEJS_14_X,
      handler: 'index.handler',
      code: lambda.Code.fromAsset('lambda'),
      environment: {
        QUEUE_URL: queueB.queueUrl,
      },
      role: lambdaRole,
    });

    // Add SQS event sources to the Lambda functions
    myLambdaA.addEventSource(new lambdaEventSources.SqsEventSource(queueA));
    myLambdaB.addEventSource(new lambdaEventSources.SqsEventSource(queueB));

    // Grant the SNS topic permission to send messages to the SQS queues
    queueA.grantSendMessages(new iam.ServicePrincipal('sns.amazonaws.com'));
    queueB.grantSendMessages(new iam.ServicePrincipal('sns.amazonaws.com'));

    // Create an S3 bucket to hold the deployment artifacts
    const bucket = new s3.Bucket(this, 'DeploymentBucket', {
      removalPolicy: cdk.RemovalPolicy.DESTROY, // Automatically delete bucket when stack is deleted
    });

    // Deploy the contents of the 'folder' directory to the S3 bucket under the 'staging' prefix
    new s3deploy.BucketDeployment(this, 'DeployStaticAssets', {
      sources: [s3deploy.Source.asset(path.join(__dirname, '../folder'))],
      destinationBucket: bucket,
      destinationKeyPrefix: 'staging/', // Optional prefix in the bucket
    });
  }
}

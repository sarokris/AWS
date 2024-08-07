import * as cdk from 'aws-cdk-lib';
import * as sns from 'aws-cdk-lib/aws-sns';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as eventSources from 'aws-cdk-lib/aws-lambda-event-sources';
import * as subscription from 'aws-cdk-lib/aws-sns-subscriptions';


import { Construct } from 'constructs';

export class SnsMessageFilterStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    //Create SNS topic
    const productLaunchTopic = new sns.Topic(this, 'ProductLaunch', {
      topicName: 'ProductLaunch',
    });

    // Create SQS Queues
    const bookQueue = new sqs.Queue(this, 'BookSubscriptionQueue');
    const gadgetQueue = new sqs.Queue(this, 'GadgetSubscriptionQueue');

    // Create Lambda Functions
    const lambda1 = new lambda.Function(this, 'book-notification', {
      runtime: lambda.Runtime.NODEJS_16_X,
      handler: 'books-subscription.handler',
      code: lambda.Code.fromAsset('lambda')
    });

    const lambda2 = new lambda.Function(this, 'gadget-notification', {
      runtime: lambda.Runtime.NODEJS_16_X,
      handler: 'gadget-subscription.handler',
      code: lambda.Code.fromAsset('lambda')
    });

    // Add SQS event sources to Lambda functions
    lambda1.addEventSource(new eventSources.SqsEventSource(bookQueue));
    lambda2.addEventSource(new eventSources.SqsEventSource(gadgetQueue));

    // Add SQS subscriptions to SNS topic with filter policies
    productLaunchTopic.addSubscription(new subscription.SqsSubscription(bookQueue,{
      filterPolicy: {
        eventType: sns.SubscriptionFilter.stringFilter({
          allowlist: ['new_books_launched']
        })
      }
    }));

    productLaunchTopic.addSubscription(new subscription.SqsSubscription(gadgetQueue,
      {
        filterPolicy: {
          eventType: sns.SubscriptionFilter.stringFilter({
            allowlist: ['new_gadget_launched']
          })
        }
      }
    ));


  }
}

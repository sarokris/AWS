import * as cdk from 'aws-cdk-lib';
import { Template } from 'aws-cdk-lib/assertions';
import * as CdkTsDemo from '../lib/cdk-ts-demo-stack';

test('Lambda function created', () => {
  const app = new cdk.App();
    // WHEN
  const stack = new CdkTsDemo.CdkTsDemoStack(app, 'MyTestStack');
    // THEN
  const template = Template.fromStack(stack);

  template.hasResourceProperties('AWS::Lambda::Function', {
    FunctionName: "demoApi",
    Handler: "health.handler",
  });
});

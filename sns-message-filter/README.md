# Welcome to SNS message filtering project

This is a demo project for CDK development with TypeScript integrated with localstack deployment to demonstrate the SNS message filtering capabilities.


## Prerequisite
- nodeJs 
- Localstack 
- Docker
- cdklocal 
- awslocal 

## Installation
- AWS CDK - https://www.npmjs.com/package/aws-cdk  
- NodeJS - https://nodejs.org/en/download/
- LocalStack - https://github.com/localstack/localst...
- CDKLocal - https://www.npmjs.com/package/aws-cdk...
- AWSLocal -  https://github.com/localstack/awscli-...


Follow the CDK deployment process explained in [this article](https://www.linkedin.com/pulse/aws-cdk-localstack-integration-simplifying-cloud-development-k-btjue/)

> Dont forget to build, bootstrap and synthesis before you deploy your application to localstack.

```
aws --endpoint-url=http://localhost:4566 sns publish \                           
 --topic-arn arn:aws:sns:ap-south-1:000000000000:ProductLaunch \
 --message "Hello from AWS CLI with message attributes" \
 --message-attributes \
    '{"productType": {"DataType": "String", "StringValue": "books"}}'
```
you can replace the arn of the topic and publish to send message to your topic for testing. 

Get the topic arn from the below command and proceed testing. Happy learning.

```
aws --endpoint-url=http://localhost:4566 sns list-topics
```
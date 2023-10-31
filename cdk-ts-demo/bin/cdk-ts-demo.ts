#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { CdkTsDemoStack } from '../lib/cdk-ts-demo-stack';

const app = new cdk.App();
new CdkTsDemoStack(app, 'CdkTsDemoStack', {
  env: { 
    account: process.env.CDK_DEFAULT_ACCOUNT, 
    region: process.env.CDK_DEFAULT_REGION
  }
});

app.synth();

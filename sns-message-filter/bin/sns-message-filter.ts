#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { SnsMessageFilterStack } from '../lib/sns-message-filter-stack';

const app = new cdk.App();
new SnsMessageFilterStack(app, 'SnsMessageFilterStack', {
  env: { 
    account: process.env.CDK_DEFAULT_ACCOUNT, 
    region: process.env.CDK_DEFAULT_REGION
  }
});

app.synth();
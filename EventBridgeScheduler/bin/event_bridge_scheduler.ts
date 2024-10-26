#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { EventBridgeSchedulerStack } from '../lib/event_bridge_scheduler-stack';

const app = new cdk.App();
new EventBridgeSchedulerStack(app, 'EventBridgeSchedulerStack', {
  env: { 
    account: process.env.CDK_DEFAULT_ACCOUNT, 
    region: process.env.CDK_DEFAULT_REGION
  }
});
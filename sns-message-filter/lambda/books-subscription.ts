import { SQSEvent } from 'aws-lambda';

exports.handler = async (event: SQSEvent) => {
  console.log('Received new book launch notification:', JSON.stringify(event, null, 2));
  event.Records.forEach(record => {
    console.log('Message from book launch queue:', record.body);
  });
};

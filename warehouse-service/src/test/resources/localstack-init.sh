#!/bin/bash

LOCALSTACK_HOST=localhost

echo "Creating SQS queues..."

SQS_QUEUES=(
  "central-service_sensor-data-storage",3
)

for queue in "${SQS_QUEUES[@]}"
do
  IFS=',' read -ra QUEUE_CONFIG <<< "$queue"

  QUEUE_NAME=${QUEUE_CONFIG[0]}
  RETRY_ATTEMPTS=${QUEUE_CONFIG[1]}

  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name="${QUEUE_NAME}_dlq"
  echo "DLQ queue [${QUEUE_NAME}_dlq] created"

  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name=${QUEUE_NAME} \
      --attributes DelaySeconds=5,RedrivePolicy="\"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-west-2:000000000000:${QUEUE_NAME}_dlq\\\",\\\"maxReceiveCount\\\":\\\"${RETRY_ATTEMPTS}\\\"}\""
  echo "Queue [${QUEUE_NAME}] created"
done

echo "Configuration completed, ready to dev!"
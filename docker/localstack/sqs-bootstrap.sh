#!/usr/bin/env bash

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name notifications-queue --region us-east-1

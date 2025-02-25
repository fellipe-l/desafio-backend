package com.backend.desafio.notification;

import com.backend.desafio.properties.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class QueueProducer {
    private final SqsClient sqsClient;
    private final SqsProperties sqsProperties;

    @Autowired
    public QueueProducer(SqsClient sqsClient, SqsProperties sqsProperties) {
        this.sqsClient = sqsClient;
        this.sqsProperties = sqsProperties;
    }

    public void produceMessage(String message) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(sqsProperties.getEndpoint() + sqsProperties.getQueueUrlPath())
                .messageBody(message)
                .build();

        sqsClient.sendMessage(request);
    }
}

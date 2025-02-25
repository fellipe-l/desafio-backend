package com.backend.desafio.notification;

import com.backend.desafio.properties.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component
public class QueueConsumer {
    private final NotificationService notificationService;
    private final SqsClient sqsClient;
    private final SqsProperties sqsProperties;

    @Autowired
    public QueueConsumer(NotificationService notificationService, SqsClient sqsClient, SqsProperties sqsProperties) {
        this.notificationService = notificationService;
        this.sqsClient = sqsClient;
        this.sqsProperties = sqsProperties;
    }

    public void consumeMessage() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsProperties.getEndpoint() + sqsProperties.getQueueUrlPath())
                .maxNumberOfMessages(1)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
        notificationService.notifyPayee(messages.getFirst().body());
    }
}

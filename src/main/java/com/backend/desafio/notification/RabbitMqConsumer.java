package com.backend.desafio.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumer {
    private final NotificationService notificationService;

    @Autowired
    public RabbitMqConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void consumeMessage(String message) {
        notificationService.notifyPayee(message);
    }
}

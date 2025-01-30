package com.backend.desafio.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final RabbitMqProducer producer;

    @Autowired
    public NotificationService(RabbitMqProducer producer) {
        this.producer = producer;
    }

    public void notifyPayee(String message) {
        producer.produceMessage(message);
    }
}

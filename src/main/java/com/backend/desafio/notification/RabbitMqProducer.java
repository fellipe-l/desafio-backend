package com.backend.desafio.notification;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProducer {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceMessage(String message) {
        rabbitTemplate.convertAndSend(
                "notifications-exchange",
                "notifications-routing-key",
                message
        );
    }
}

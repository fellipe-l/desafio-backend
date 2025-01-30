package com.backend.desafio.notification;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RabbitMqConsumer {
    public void consumeMessage(String message) {
        String uri = "https://util.devi.tools/api/v1/notify";
        Notification request = new Notification(message);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject(uri, request, Void.class);
    }
}

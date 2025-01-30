package com.backend.desafio.notification;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    private int currentRetries = 0;

    public void notifyPayee(String message) {
        try {
            String uri = "https://util.devi.tools/api/v1/notify";
            Notification request = new Notification(message);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.postForObject(uri, request, Void.class);

            resetCurrentRetries();
        } catch (RestClientException ex) {
            if (currentRetries >= 5) {
                resetCurrentRetries();
                return;
            }

            currentRetries++;
            notifyPayee(message);
        }
    }

    private void resetCurrentRetries() {
        currentRetries = 0;
    }
}

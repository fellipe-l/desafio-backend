package com.backend.desafio.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sqs")
public class SqsProperties {
    private String queueUrlPath;
    private String endpoint;

    public String getQueueUrlPath() {
        return queueUrlPath;
    }

    public void setQueueUrlPath(String queueUrlPath) {
        this.queueUrlPath = queueUrlPath;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}

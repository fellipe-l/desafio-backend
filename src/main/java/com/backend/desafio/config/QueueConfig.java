package com.backend.desafio.config;

import com.backend.desafio.properties.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class QueueConfig {
    private final SqsProperties sqsProperties;

    @Autowired
    public QueueConfig(SqsProperties sqsProperties) {
        this.sqsProperties = sqsProperties;
    }

    @Bean
    public SqsClient amazonSQSClient() throws URISyntaxException {
        return SqsClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .endpointOverride(new URI(sqsProperties.getEndpoint()))
                .region(Region.US_EAST_1)
                .build();
    }
}

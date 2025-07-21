package com.example.moneybutton.feedback;

import com.example.moneybutton.SecretsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Bean
    public S3Client s3Client(SecretsProperties secrets) {
        return S3Client.builder()
                .endpointOverride(java.net.URI.create(secrets.getMinioEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(secrets.getMinioAccessKey(), secrets.getMinioSecretKey())))
                .region(Region.US_EAST_1)
                .build();
    }
}

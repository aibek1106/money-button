package com.example.moneybutton;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "secrets")
public class SecretsProperties {
    private String birdeyeKey;
    private String heliusKey;
    private String tgToken;
    private String tgChat;
    private String clickhouseUrl;
    private String minioEndpoint;
    private String minioAccessKey;
    private String minioSecretKey;
    private String minioBucket;

    public String getBirdeyeKey() {
        return birdeyeKey;
    }

    public void setBirdeyeKey(String birdeyeKey) {
        this.birdeyeKey = birdeyeKey;
    }

    public String getHeliusKey() {
        return heliusKey;
    }

    public void setHeliusKey(String heliusKey) {
        this.heliusKey = heliusKey;
    }

    public String getTgToken() {
        return tgToken;
    }

    public void setTgToken(String tgToken) {
        this.tgToken = tgToken;
    }

    public String getTgChat() {
        return tgChat;
    }

    public void setTgChat(String tgChat) {
        this.tgChat = tgChat;
    }

    public String getClickhouseUrl() {
        return clickhouseUrl;
    }

    public void setClickhouseUrl(String clickhouseUrl) {
        this.clickhouseUrl = clickhouseUrl;
    }
    public String getMinioEndpoint() {
        return minioEndpoint;
    }

    public void setMinioEndpoint(String minioEndpoint) {
        this.minioEndpoint = minioEndpoint;
    }

    public String getMinioAccessKey() {
        return minioAccessKey;
    }

    public void setMinioAccessKey(String minioAccessKey) {
        this.minioAccessKey = minioAccessKey;
    }

    public String getMinioSecretKey() {
        return minioSecretKey;
    }

    public void setMinioSecretKey(String minioSecretKey) {
        this.minioSecretKey = minioSecretKey;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public void setMinioBucket(String minioBucket) {
        this.minioBucket = minioBucket;
    }
}

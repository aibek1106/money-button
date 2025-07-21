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
}

package com.example.moneybutton.onnx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MlScorerIntegrationTest {

    @Autowired
    MlScorerService scorer;

    @Autowired
    TestListener listener;

    @Test
    void tokenAboveThresholdTriggersAlert() {
        TokenFeatures features = new TokenFeatures(new float[]{1f,1f,1f,1f,1f});
        scorer.score(features);
        assertTrue(listener.received);
    }

    static class TestListener {
        volatile boolean received = false;
        @EventListener
        public void handle(AlertEvent evt) {
            received = true;
        }
    }

    @Configuration
    static class Config {
        @Bean
        TestListener testListener() { return new TestListener(); }
    }
}

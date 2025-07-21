package com.example.moneybutton.helius;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.helius.dto.MintInfoDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeliusClientLatencyTest {

    private MockWebServer server;
    private HeliusClient client;

    @BeforeEach
    void setup() throws IOException {
        server = new MockWebServer();
        // respond with 20ms delay to simulate network
        MockResponse response = new MockResponse()
                .setBody("{\"mint\":\"test\"}")
                .addHeader("Content-Type", "application/json")
                .setBodyDelay(20, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 20; i++) {
            server.enqueue(response.clone());
        }
        server.start();

        WebClient.Builder builder = WebClient.builder().baseUrl(server.url("/").toString());
        SecretsProperties props = new SecretsProperties();
        props.setHeliusKey("test");
        client = new HeliusClient(builder, props);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void tenParallelRequestsUnderThreeHundredMs() throws Exception {
        List<CompletableFuture<MintInfoDto>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            futures.add(client.getMintInfo("mint" + i));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        long duration = System.currentTimeMillis() - start;
        assertTrue(duration <= 300, "Parallel execution took too long: " + duration);
    }
}

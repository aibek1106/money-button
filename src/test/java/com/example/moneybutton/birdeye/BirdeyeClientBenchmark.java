package com.example.moneybutton.birdeye;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.openjdk.jmh.annotations.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MINUTES)
@BenchmarkMode(Mode.Throughput)
public class BirdeyeClientBenchmark {

    private MockWebServer server;
    private BirdeyeClient client;

    @Setup
    public void setup() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"symbol\":\"SOL\",\"price\":1.0,\"volume24h\":1.0,\"marketCap\":1.0}]")
                .addHeader("Content-Type", "application/json"));
        server.start();

        WebClient.Builder builder = WebClient.builder().baseUrl(server.url("/").toString());
        SecretsProperties props = new SecretsProperties();
        props.setBirdeyeKey("test");
        client = new BirdeyeClient(builder, props);
    }

    @TearDown
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Benchmark
    public List<TokenMarketDto> measure() {
        return client.getTopTokens(1);
    }
}

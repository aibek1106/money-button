package com.example.moneybutton.birdeye;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class BirdeyeClient {

    private final WebClient webClient;
    private final SecretsProperties secrets;

    public BirdeyeClient(WebClient.Builder builder, SecretsProperties secrets) {
        this.webClient = builder.baseUrl("https://public-api.birdeye.so").build();
        this.secrets = secrets;
    }

    public List<TokenMarketDto> getTopTokens(int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/defi/tokenlist")
                        .queryParam("limit", limit)
                        .build())
                .header("X-API-KEY", secrets.getBirdeyeKey())
                .retrieve()
                .bodyToFlux(TokenMarketDto.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .collectList()
                .block();
    }
}

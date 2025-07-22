package com.example.moneybutton.birdeye;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class BirdeyeClient {

    private final WebClient webClient;
    private final SecretsProperties secrets;
    private static final Logger log = LoggerFactory.getLogger(BirdeyeClient.class);

    public BirdeyeClient(WebClient.Builder builder, SecretsProperties secrets) {
        this.webClient = builder.baseUrl("https://public-api.birdeye.so").build();
        this.secrets = secrets;
    }

    public List<TokenMarketDto> getTopTokens(int limit) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/defi/tokenlist")
                            .queryParam("chain", "solana")
                            .queryParam("limit", limit)
                            .build())
                    .header("X-API-KEY", secrets.getBirdeyeKey())
                    .retrieve()
                    .bodyToFlux(TokenMarketDto.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.warn("Birdeye API error {} - {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}

package com.example.moneybutton.helius;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.helius.dto.HolderStatsDto;
import com.example.moneybutton.helius.dto.LpLockInfoDto;
import com.example.moneybutton.helius.dto.MintInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class HeliusClient {

    private final WebClient webClient;
    private final SecretsProperties secrets;

    public HeliusClient(WebClient.Builder builder, SecretsProperties secrets) {
        this.webClient = builder.baseUrl("https://api.helius.xyz").build();
        this.secrets = secrets;
    }

    public CompletableFuture<MintInfoDto> getMintInfo(String mint) {
        return request("/mint/" + mint + "/info", MintInfoDto.class);
    }

    public CompletableFuture<LpLockInfoDto> getLpLockInfo(String mint) {
        return request("/mint/" + mint + "/lp-lock", LpLockInfoDto.class);
    }

    public CompletableFuture<HolderStatsDto> getHolderStats(String mint) {
        return request("/mint/" + mint + "/holders", HolderStatsDto.class);
    }

    private <T> CompletableFuture<T> request(String path, Class<T> clazz) {
        Mono<T> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("api-key", secrets.getHeliusKey())
                        .build())
                .retrieve()
                .bodyToMono(clazz)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
        return mono.toFuture();
    }
}

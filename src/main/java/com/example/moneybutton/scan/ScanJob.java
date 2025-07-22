package com.example.moneybutton.scan;

import com.example.moneybutton.alert.AlertStore;
import com.example.moneybutton.alert.TokenAlert;
import com.example.moneybutton.alert.TelegramNotifier;
import com.example.moneybutton.birdeye.BirdeyeClient;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import com.example.moneybutton.helius.HeliusClient;
import com.example.moneybutton.helius.dto.HolderStatsDto;
import com.example.moneybutton.helius.dto.LpLockInfoDto;
import com.example.moneybutton.helius.dto.MintInfoDto;
import com.example.moneybutton.onnx.MlScorerService;
import com.example.moneybutton.onnx.TokenFeatures;
import com.example.moneybutton.rules.TokenFilterService;
import com.example.moneybutton.rules.TokenInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ScanJob {
    private final BirdeyeClient birdeyeClient;
    private final TokenFilterService filterService;
    private final HeliusClient heliusClient;
    private final MlScorerService scorer;
    private final TelegramNotifier notifier;
    private final AlertStore store;

    public ScanJob(BirdeyeClient birdeyeClient,
                   TokenFilterService filterService,
                   HeliusClient heliusClient,
                   MlScorerService scorer,
                   TelegramNotifier notifier,
                   AlertStore store) {
        this.birdeyeClient = birdeyeClient;
        this.filterService = filterService;
        this.heliusClient = heliusClient;
        this.scorer = scorer;
        this.notifier = notifier;
        this.store = store;
    }

    @Scheduled(fixedRate = 60_000)
    public void run() {
        List<TokenMarketDto> tokens = birdeyeClient.getTopTokens(500);
        for (TokenMarketDto dto : tokens) {
            TokenInfo info = enrich(dto);
            if (!filterService.isPass(info)) {
                continue;
            }
            TokenFeatures features = toFeatures(dto);
            double score = scorer.score(features);
            if (score >= MlScorerService.THRESHOLD) {
                TokenAlert alert = new TokenAlert(dto.getSymbol(), score, Instant.now());
                store.add(alert);
                notifier.sendAlert(alert);
            }
        }
    }

    private TokenInfo enrich(TokenMarketDto dto) {
        TokenInfo info = new TokenInfo();
        info.setFdv(dto.getMarketCap());
        info.setLiquidity(dto.getVolume24h());
        info.setVolume1hChange(dto.getVolume24h());
        try {
            HolderStatsDto hs = heliusClient.getHolderStats(dto.getSymbol()).get();
            info.setHolders(hs.getHolderCount());
        } catch (Exception e) {
            info.setHolders(0);
        }
        try {
            MintInfoDto mi = heliusClient.getMintInfo(dto.getSymbol()).get();
            info.setAuthorityBurned(mi.getMint() == null);
        } catch (Exception e) {
            info.setAuthorityBurned(false);
        }
        try {
            LpLockInfoDto lp = heliusClient.getLpLockInfo(dto.getSymbol()).get();
            if ("locked".equalsIgnoreCase(lp.getStatus())) {
                info.setLpLockMonths(12);
            } else {
                info.setLpLockMonths(0);
            }
        } catch (Exception e) {
            info.setLpLockMonths(0);
        }
        info.setVolume24h(dto.getVolume24h());
        return info;
    }

    private TokenFeatures toFeatures(TokenMarketDto t) {
        float[] v = new float[5];
        v[0] = (float) t.getPrice();
        v[1] = (float) t.getVolume24h();
        v[2] = (float) t.getMarketCap();
        v[3] = (float) (t.getPrice() * t.getVolume24h());
        v[4] = (float) Math.log(Math.max(t.getMarketCap(), 1));
        return new TokenFeatures(v);
    }
}

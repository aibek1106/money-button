package com.example.moneybutton.features;

import com.example.moneybutton.birdeye.BirdeyeClient;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class FeatureExtractor {

    private final BirdeyeClient birdeyeClient;
    private final JdbcTemplate jdbcTemplate;

    public FeatureExtractor(BirdeyeClient birdeyeClient, JdbcTemplate jdbcTemplate) {
        this.birdeyeClient = birdeyeClient;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void extract() {
        List<TokenMarketDto> tokens = birdeyeClient.getTopTokens(5);
        String sql = "INSERT INTO features_5m (ts, symbol, price, volume24h, marketCap, priceVolume, logMarketCap) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, tokens, tokens.size(), (ps, t) -> {
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            ps.setString(2, t.getSymbol());
            ps.setDouble(3, t.getPrice());
            ps.setDouble(4, t.getVolume24h());
            ps.setDouble(5, t.getMarketCap());
            ps.setDouble(6, t.getPrice() * t.getVolume24h());
            ps.setDouble(7, Math.log(Math.max(t.getMarketCap(), 1)));
        });
    }
}

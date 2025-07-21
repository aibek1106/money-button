package com.example.moneybutton.features;

import com.example.moneybutton.birdeye.BirdeyeClient;
import com.example.moneybutton.birdeye.dto.TokenMarketDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "secrets", name = "clickhouse-enabled", havingValue = "true", matchIfMissing = true)
public class FeatureExtractor {

    private final BirdeyeClient birdeyeClient;
    private final DataSource dataSource;

    public FeatureExtractor(BirdeyeClient birdeyeClient, DataSource dataSource) {
        this.birdeyeClient = birdeyeClient;
        this.dataSource = dataSource;
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void extract() {
        List<TokenMarketDto> tokens = birdeyeClient.getTopTokens(5);
        String sql = "INSERT INTO features_5m (ts, symbol, price, volume24h, marketCap, priceVolume, logMarketCap) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (TokenMarketDto t : tokens) {
                ps.setTimestamp(1, Timestamp.from(Instant.now()));
                ps.setString(2, t.getSymbol());
                ps.setDouble(3, t.getPrice());
                ps.setDouble(4, t.getVolume24h());
                ps.setDouble(5, t.getMarketCap());
                ps.setDouble(6, t.getPrice() * t.getVolume24h());
                ps.setDouble(7, Math.log(Math.max(t.getMarketCap(), 1)));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            // simple error log
            e.printStackTrace();
        }
    }
}

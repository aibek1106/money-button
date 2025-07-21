package com.example.moneybutton.features;

import com.example.moneybutton.SecretsProperties;
import com.clickhouse.jdbc.ClickHouseDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class ClickHouseConfig {

    private final SecretsProperties secrets;

    public ClickHouseConfig(SecretsProperties secrets) {
        this.secrets = secrets;
    }

    @Bean
    public DataSource clickHouseDataSource() {
        return new ClickHouseDataSource(secrets.getClickhouseUrl());
    }

    @PostConstruct
    public void init() throws Exception {
        try (Connection conn = clickHouseDataSource().getConnection();
             Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS features_5m (" +
                    "ts DateTime, " +
                    "symbol String, " +
                    "price Float64, " +
                    "volume24h Float64, " +
                    "marketCap Float64, " +
                    "priceVolume Float64, " +
                    "logMarketCap Float64" +
                    ") ENGINE = MergeTree() ORDER BY ts TTL ts + INTERVAL 7 DAY");
        }
    }
}

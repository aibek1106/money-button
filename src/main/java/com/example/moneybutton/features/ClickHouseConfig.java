package com.example.moneybutton.features;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.example.moneybutton.SecretsProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class ClickHouseConfig {

    private final SecretsProperties secrets;

    public ClickHouseConfig(SecretsProperties secrets) {
        this.secrets = secrets;
    }

    @Bean
    public DataSource clickHouseDataSource() throws SQLException {
        return new ClickHouseDataSource(secrets.getClickhouseUrl());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void init() throws Exception {
        JdbcTemplate template = jdbcTemplate(clickHouseDataSource());
        template.execute("CREATE TABLE IF NOT EXISTS features_5m (" +
                "ts DateTime, " +
                "symbol String, " +
                "price Float64, " +
                "volume24h Float64, " +
                "marketCap Float64, " +
                "priceVolume Float64, " +
                "logMarketCap Float64" +
                ") ENGINE = MergeTree() ORDER BY ts TTL ts + INTERVAL 7 DAY");
        template.execute("CREATE TABLE IF NOT EXISTS feedback (" +
                "ts DateTime, " +
                "f1 Float32, " +
                "f2 Float32, " +
                "f3 Float32, " +
                "f4 Float32, " +
                "f5 Float32, " +
                "outcome UInt8, " +
                "exported UInt8" +
                ") ENGINE = MergeTree() ORDER BY ts");
    }
}

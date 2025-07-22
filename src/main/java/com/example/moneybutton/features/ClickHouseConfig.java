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
    private final JdbcTemplate jdbcTemplate;

    public ClickHouseConfig(SecretsProperties secrets, JdbcTemplate jdbcTemplate) {
        this.secrets = secrets;
        this.jdbcTemplate = jdbcTemplate;
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
    public void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS features_5m (...)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS feedback (...)");
    }
}
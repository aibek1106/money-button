package com.example.moneybutton.features;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.example.moneybutton.SecretsProperties;
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS features_5m (...)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS feedback (...)");
        return jdbcTemplate;
    }
}
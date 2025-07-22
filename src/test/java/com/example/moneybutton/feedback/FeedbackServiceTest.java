package com.example.moneybutton.feedback;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.onnx.TokenFeatures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.services.s3.S3Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedbackServiceTest {
    private DataSource ds;
    private FeedbackService service;

    @BeforeEach
    void setup() throws Exception {
        ds = new org.h2.jdbcx.JdbcDataSource();
        ((org.h2.jdbcx.JdbcDataSource) ds).setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        try (Connection conn = ds.getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE feedback (ts TIMESTAMP,f1 REAL,f2 REAL,f3 REAL,f4 REAL,f5 REAL,outcome BOOLEAN,exported BOOLEAN)");
        }
        SecretsProperties secrets = new SecretsProperties();
        secrets.setMinioBucket("bucket");
        S3Client s3 = S3Client.builder().region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .credentialsProvider(software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider.create())
                .endpointOverride(java.net.URI.create("http://localhost:0"))
                .build();
        service = new FeedbackService(new JdbcTemplate(ds), s3, secrets);
    }

    @Test
    void saveFeedbackInsertsRow() throws Exception {
        TokenFeatures features = new TokenFeatures(new float[]{1,2,3,4,5});
        service.saveFeedback(new FeedbackDto(features, true));
        try (Connection conn = ds.getConnection(); Statement st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT COUNT(*) FROM feedback");
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void exportLogsRetrainWhenThresholdMet() throws Exception {
        TokenFeatures f = new TokenFeatures(new float[]{1,1,1,1,1});
        for (int i = 0; i < 200; i++) {
            service.saveFeedback(new FeedbackDto(f, true));
        }
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        service.exportNewLabels();
        System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out)));
        String log = out.toString();
        org.junit.jupiter.api.Assertions.assertTrue(log.contains("retrain scheduled"));
    }
}

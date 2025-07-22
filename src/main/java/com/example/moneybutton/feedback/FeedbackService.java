package com.example.moneybutton.feedback;

import com.example.moneybutton.SecretsProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
public class FeedbackService {
    private final JdbcTemplate jdbcTemplate;
    private final S3Client s3Client;
    private final String bucket;

    public FeedbackService(JdbcTemplate jdbcTemplate, S3Client s3Client, SecretsProperties secrets) {
        this.jdbcTemplate = jdbcTemplate;
        this.s3Client = s3Client;
        this.bucket = secrets.getMinioBucket();
    }

    public void saveFeedback(FeedbackDto dto) {
        String sql = "INSERT INTO feedback (ts,f1,f2,f3,f4,f5,outcome,exported) VALUES (?,?,?,?,?,?,?,0)";
        float[] v = dto.getFeatures().getValues();
        jdbcTemplate.update(sql, ps -> {
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            for (int i = 0; i < 5; i++) {
                ps.setFloat(i + 2, v[i]);
            }
            ps.setBoolean(7, dto.isOutcome());
        });
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void exportNewLabels() {
        String select = "SELECT ts,f1,f2,f3,f4,f5,outcome FROM feedback WHERE exported=0";
        String update = "ALTER TABLE feedback UPDATE exported=1 WHERE exported=0";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        java.util.List<java.util.Map<String, Object>> rows = jdbcTemplate.queryForList(select);
        for (java.util.Map<String, Object> row : rows) {
            writer.printf("%s,%f,%f,%f,%f,%f,%d\n",
                    ((Timestamp) row.get("ts")).toInstant(),
                    ((Number) row.get("f1")).floatValue(),
                    ((Number) row.get("f2")).floatValue(),
                    ((Number) row.get("f3")).floatValue(),
                    ((Number) row.get("f4")).floatValue(),
                    ((Number) row.get("f5")).floatValue(),
                    ((Number) row.get("outcome")).intValue());
        }
        writer.flush();
        if (!rows.isEmpty()) {
            String key = "labels-" + DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + ".csv";
            s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
                    RequestBody.fromBytes(baos.toByteArray()));
            jdbcTemplate.update(update);
            if (rows.size() >= 200) {
                System.out.println("retrain scheduled");
            }
        }
    }
}

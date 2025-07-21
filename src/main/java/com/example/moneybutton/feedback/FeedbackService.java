package com.example.moneybutton.feedback;

import com.example.moneybutton.SecretsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
@ConditionalOnProperty(prefix = "secrets", name = "clickhouse-enabled", havingValue = "true", matchIfMissing = true)
public class FeedbackService {
    private final DataSource dataSource;
    private final S3Client s3Client;
    private final String bucket;

    public FeedbackService(DataSource dataSource, S3Client s3Client, SecretsProperties secrets) {
        this.dataSource = dataSource;
        this.s3Client = s3Client;
        this.bucket = secrets.getMinioBucket();
    }

    public void saveFeedback(FeedbackDto dto) {
        String sql = "INSERT INTO feedback (ts,f1,f2,f3,f4,f5,outcome,exported) VALUES (?,?,?,?,?,?,?,0)";
        float[] v = dto.getFeatures().getValues();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            for (int i = 0; i < 5; i++) {
                ps.setFloat(i + 2, v[i]);
            }
            ps.setBoolean(7, dto.isOutcome());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void exportNewLabels() {
        String select = "SELECT ts,f1,f2,f3,f4,f5,outcome FROM feedback WHERE exported=0";
        String update = "ALTER TABLE feedback UPDATE exported=1 WHERE exported=0";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(select);
             ResultSet rs = ps.executeQuery()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);
            int count = 0;
            while (rs.next()) {
                writer.printf("%s,%f,%f,%f,%f,%f,%d\n",
                        rs.getTimestamp(1).toInstant(),
                        rs.getFloat(2), rs.getFloat(3), rs.getFloat(4), rs.getFloat(5), rs.getFloat(6), rs.getBoolean(7) ? 1 : 0);
                count++;
            }
            writer.flush();
            if (count > 0) {
                String key = "labels-" + DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + ".csv";
                s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
                        RequestBody.fromBytes(baos.toByteArray()));
                conn.createStatement().execute(update);
                if (count >= 200) {
                    System.out.println("retrain scheduled");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

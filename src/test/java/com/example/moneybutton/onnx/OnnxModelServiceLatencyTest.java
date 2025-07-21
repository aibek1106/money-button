package com.example.moneybutton.onnx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnnxModelServiceLatencyTest {

    @Test
    void averageLatencyUnder50Ms() {
        OnnxModelService service = new OnnxModelService();
        float[] features = new float[] {0.1f, 0.2f, 0.3f, 0.4f, 0.5f};
        long total = 0;
        for (int i = 0; i < 20; i++) {
            long start = System.currentTimeMillis();
            service.predict(features);
            total += System.currentTimeMillis() - start;
        }
        service.close();
        double avg = total / 20.0;
        assertTrue(avg < 50, "Average latency " + avg + "ms is too high");
    }
}

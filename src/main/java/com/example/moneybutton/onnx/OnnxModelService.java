package com.example.moneybutton.onnx;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Service
public class OnnxModelService {

    private final OrtEnvironment env;
    private final OrtSession session;

    public OnnxModelService() {
        try {
            this.env = OrtEnvironment.getEnvironment();
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("model.onnx")) {
                if (is == null) {
                    throw new IllegalStateException("model.onnx not found");
                }
                byte[] bytes = is.readAllBytes();
                this.session = env.createSession(bytes, new OrtSession.SessionOptions());
            }
        } catch (OrtException | IOException e) {
            throw new RuntimeException("Failed to load ONNX model", e);
        }
    }

    public float predict(float[] features) {
        try (OnnxTensor input = OnnxTensor.createTensor(env, new float[][] { features })) {
            Map<String, OnnxTensor> inputs = Collections.singletonMap("input", input);
            OrtSession.Result result = session.run(inputs);
            float[][] output = (float[][]) result.get(0).getValue();
            return output[0][0];
        } catch (OrtException e) {
            throw new RuntimeException("ONNX inference failed", e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            session.close();
            env.close();
        } catch (OrtException e) {
            // ignore
        }
    }
}

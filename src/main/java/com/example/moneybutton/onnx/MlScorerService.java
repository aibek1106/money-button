package com.example.moneybutton.onnx;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MlScorerService {
    static final double THRESHOLD = 0.70d;

    private final OnnxModelService modelService;
    private final ApplicationEventPublisher publisher;

    public MlScorerService(OnnxModelService modelService, ApplicationEventPublisher publisher) {
        this.modelService = modelService;
        this.publisher = publisher;
    }

    public double score(TokenFeatures features) {
        float result = modelService.predict(features.getValues());
        if (result >= THRESHOLD) {
            publisher.publishEvent(new AlertEvent(features, result));
        }
        return result;
    }
}

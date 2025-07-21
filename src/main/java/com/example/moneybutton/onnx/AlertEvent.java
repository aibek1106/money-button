package com.example.moneybutton.onnx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertEvent {
    private final TokenFeatures features;
    private final double score;

    @JsonCreator
    public AlertEvent(@JsonProperty("features") TokenFeatures features,
                      @JsonProperty("score") double score) {
        this.features = features;
        this.score = score;
    }

    public TokenFeatures getFeatures() {
        return features;
    }

    public double getScore() {
        return score;
    }
}

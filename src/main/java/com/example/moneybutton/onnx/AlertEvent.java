package com.example.moneybutton.onnx;

public class AlertEvent {
    private final TokenFeatures features;
    private final double score;

    public AlertEvent(TokenFeatures features, double score) {
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

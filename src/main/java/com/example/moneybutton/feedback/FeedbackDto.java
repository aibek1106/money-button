package com.example.moneybutton.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.moneybutton.onnx.TokenFeatures;

public class FeedbackDto {
    private final TokenFeatures features;
    private final boolean outcome;

    @JsonCreator
    public FeedbackDto(@JsonProperty("features") TokenFeatures features,
                       @JsonProperty("outcome") boolean outcome) {
        this.features = features;
        this.outcome = outcome;
    }

    public TokenFeatures getFeatures() {
        return features;
    }

    public boolean isOutcome() {
        return outcome;
    }
}

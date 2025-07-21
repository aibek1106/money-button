package com.example.moneybutton.onnx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenFeatures {
    private final float[] values;

    @JsonCreator
    public TokenFeatures(@JsonProperty("values") float[] values) {
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }
}

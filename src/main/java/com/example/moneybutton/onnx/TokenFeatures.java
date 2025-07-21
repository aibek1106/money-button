package com.example.moneybutton.onnx;

public class TokenFeatures {
    private final float[] values;

    public TokenFeatures(float[] values) {
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }
}

package com.example.moneybutton.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TokenAlert {
    private final String symbol;
    private final double score;
    private final Instant timestamp;

    @JsonCreator
    public TokenAlert(@JsonProperty("symbol") String symbol,
                      @JsonProperty("score") double score,
                      @JsonProperty("timestamp") Instant timestamp) {
        this.symbol = symbol;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getScore() {
        return score;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

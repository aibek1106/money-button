package com.example.moneybutton.alert;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertStore {
    private final List<TokenAlert> alerts = Collections.synchronizedList(new ArrayList<>());

    public void add(TokenAlert alert) {
        alerts.add(alert);
    }

    public List<TokenAlert> since(Instant since) {
        return alerts.stream()
                .filter(a -> a.getTimestamp().isAfter(since))
                .collect(Collectors.toList());
    }
}

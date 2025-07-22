package com.example.moneybutton.alert.api;

import com.example.moneybutton.alert.AlertStore;
import com.example.moneybutton.alert.TokenAlert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertStore store;

    public AlertController(AlertStore store) {
        this.store = store;
    }

    @GetMapping
    public List<TokenAlert> getAlerts(@RequestParam("since") long since) {
        return store.since(Instant.ofEpochSecond(since));
    }
}

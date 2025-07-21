package com.example.moneybutton.alert;

import com.example.moneybutton.onnx.AlertEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AlertEventBridge {
    private final AlertBus bus;

    public AlertEventBridge(AlertBus bus) {
        this.bus = bus;
    }

    @EventListener
    public void handle(AlertEvent event) {
        bus.publish(event);
    }
}

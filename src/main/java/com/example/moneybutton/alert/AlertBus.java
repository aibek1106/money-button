package com.example.moneybutton.alert;

import com.example.moneybutton.onnx.AlertEvent;
import java.util.function.Consumer;

public interface AlertBus {
    void publish(AlertEvent event);
    void subscribe(Consumer<AlertEvent> handler);
}

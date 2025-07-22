package com.example.moneybutton.alert;

import com.example.moneybutton.SecretsProperties;
import com.example.moneybutton.onnx.AlertEvent;
import com.example.moneybutton.alert.TokenAlert;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TelegramNotifier {

    private final OkHttpClient client;
    private final SecretsProperties secrets;

    public TelegramNotifier(AlertBus bus, OkHttpClient client, SecretsProperties secrets) {
        this.client = client;
        this.secrets = secrets;
        bus.subscribe(this::notify);
    }

    private void notify(AlertEvent event) {
        String text = String.format("Score: %.2f", event.getScore());
        post(text);
    }

    public void sendAlert(TokenAlert alert) {
        String escaped = alert.getSymbol().replace("_", "\\_");
        String text = String.format("*%s* scored %.2f", escaped, alert.getScore());
        post(text);
    }

    private void post(String text) {
        String url = "https://api.telegram.org/bot" + secrets.getTgToken() + "/sendMessage";
        String json = String.format("{\"chat_id\":\"%s\",\"text\":\"%s\",\"parse_mode\":\"MarkdownV2\"}",
                secrets.getTgChat(), text);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            // ignore response body
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

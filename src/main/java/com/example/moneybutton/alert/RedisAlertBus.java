package com.example.moneybutton.alert;

import com.example.moneybutton.onnx.AlertEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class RedisAlertBus implements AlertBus {

    private final StringRedisTemplate template;
    private final RedisMessageListenerContainer container;
    private final ObjectMapper mapper;
    private final List<Consumer<AlertEvent>> subscribers = new ArrayList<>();
    private final ChannelTopic topic = new ChannelTopic("alerts");

    public RedisAlertBus(StringRedisTemplate template,
                         RedisConnectionFactory factory,
                         ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
        this.container = new RedisMessageListenerContainer();
        this.container.setConnectionFactory(factory);
    }

    @PostConstruct
    public void start() {
        MessageListenerAdapter adapter = new MessageListenerAdapter((MessageListener) (message, pattern) -> {
            try {
                String body = template.getStringSerializer().deserialize(message.getBody());
                AlertEvent event = mapper.readValue(body, AlertEvent.class);
                for (Consumer<AlertEvent> sub : subscribers) {
                    sub.accept(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        container.addMessageListener(adapter, topic);
        container.afterPropertiesSet();
        container.start();
    }

    @PreDestroy
    public void stop() {
        container.stop();
    }

    @Override
    public void publish(AlertEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            template.convertAndSend(topic.getTopic(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize alert", e);
        }
    }

    @Override
    public void subscribe(Consumer<AlertEvent> handler) {
        subscribers.add(handler);
    }

    private interface MessageListener {
        void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern);
    }
}

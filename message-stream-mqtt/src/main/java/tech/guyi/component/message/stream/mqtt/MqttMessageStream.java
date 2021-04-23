package tech.guyi.component.message.stream.mqtt;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.mqtt.attach.MqttQosAttachKey;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * MQTT消息流实现
 * @author guyi
 * @date 2021/4/23 10:17
 */
@Slf4j
public class MqttMessageStream implements MessageStream<Boolean> {

    @Override
    public @NonNull String getName() {
        return "mqtt";
    }

    @Resource
    private MessageStreamWorker worker;
    @Resource
    private MqttConfiguration configuration;

    private boolean close;
    private MqttClient client;
    private ScheduledFuture<?> retry;

    private void connect(){
        try {
            this.client = new MqttClient(this.configuration.getServer(), this.configuration.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            Optional.ofNullable(this.configuration.getUsername())
                    .ifPresent(options::setUserName);
            Optional.ofNullable(this.configuration.getPassword())
                    .map(String::toCharArray)
                    .ifPresent(options::setPassword);
            this.client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
            this.retry();
        }
    }

    private void retry() {
        if (!close) {
            this.retry = worker.schedule(this::connect, configuration.getRetryDelay(), TimeUnit.SECONDS);
        }
    }

    private String replaceTopic(String topic) {
        if ("*".equals(topic)) {
            return "#";
        }
        return topic.replaceAll("\\*{2}", "#").replaceAll("\\*","+");
    }

    private void callback(Consumer<Message> receiver) {
        this.client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (retry != null) {
                    retry.cancel(true);
                    retry = null;
                }
                log.info("MQTT connection success {}", configuration.getServer());
            }

            @Override
            public void connectionLost(Throwable cause) {
                retry();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                receiver.accept(new Message(topic, message.getPayload(), Collections.singletonMap(MqttQosAttachKey.class, message.getQos())));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    @Override
    @SneakyThrows
    public void register(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        this.client.subscribe(
                this.replaceTopic(topic),
                Optional.ofNullable(attach.get(MqttQosAttachKey.class))
                        .map(Object::toString)
                        .map(Integer::parseInt)
                        .orElse(configuration.getQos())
        );
    }

    @Override
    public void open(Consumer<Message> receiver) {
        this.connect();
        this.callback(receiver);
    }

    @Override
    @SneakyThrows
    public void unregister(String topic, Map<Class<? extends AttachKey>, Object> attach) {
        this.client.unsubscribe(this.replaceTopic(topic));
    }

    @Override
    @SneakyThrows
    public void close() {
        if (this.client != null){
            this.close = true;
            if (this.client.isConnected()) {
                this.client.disconnect();
            }
            this.client.close();
        }
    }

    @Override
    public Optional<Boolean> publish(Message message) {
        return Optional.ofNullable(this.client)
                .filter(MqttClient::isConnected)
                .map(client -> {
                    try {
                        client.publish(message.getTopic(), message.getBytes(), configuration.getQos(), configuration.isRetained());
                        return true;
                    } catch (MqttException e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }

}

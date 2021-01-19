package tech.guyi.component.message.stream.websocket;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.websocket.connection.WebsocketConnection;
import tech.guyi.component.message.stream.websocket.exception.ConnectionNotReadyException;
import tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutors;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 基于Websocket实现的消息流
 * @author guyi
 * @date 2021/1/18 14:54
 */
@Slf4j
public class WebSocketMessageStream implements MessageStream {

    @Resource
    private TopicHandlerFactory factory;
    @Resource
    private WebSocketConfiguration configuration;
    @Resource
    private WebSocketServerExecutors executors;

    // Websocket连接
    private WebsocketConnection connection;

    // 创建连接
    private WebsocketConnection createConnection(Consumer<Message> receiver) throws URISyntaxException {
        return new WebsocketConnection(
                new URI(this.executors.replace(this.configuration.getServer())),
                origin -> {
                    byte[] bytes = origin.getBytes(StandardCharsets.UTF_8);
                    receiver.accept(new Message(this.factory.get().getTopic(bytes), bytes));
                },
                e -> log.info("WebSocket建立建立完成"),
                () -> this.connect(receiver),
                e -> {
                    log.error("WebSocket连接异常", e);
                    this.connect(receiver);
                }
        );
    }

    // 连接服务器
    @SneakyThrows
    private void connect(Consumer<Message> receiver) {
        if (connection != null && connection.isOpen()){
            connection.close();
        }
        connection = this.createConnection(receiver);
        connection.connectBlocking();
    }

    @Override
    public @NonNull String getName() {
        return "websocket";
    }

    @Override
    public void close() {
        Optional.ofNullable(this.connection)
                .ifPresent(WebSocketClient::close);
    }

    @Override
    public void register(String topic) {

    }

    @Override
    public void unregister(String topic) {

    }

    @Override
    @SneakyThrows
    public void open(Consumer<Message> receiver) {
        this.connect(receiver);
    }

    @Override
    public void publish(Message message) {
        if (!this.connection.isOpen()){
            throw new ConnectionNotReadyException();
        }
        this.connection.send(this.factory.get().setTopic(message.getTopic(),message.getBytes()));
    }

}

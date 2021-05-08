package tech.guyi.component.message.stream.websocket;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.stream.MessageReceiver;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.websocket.connection.WebsocketConnection;
import tech.guyi.component.message.stream.websocket.exception.ConnectionNotReadyException;
import tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutors;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 基于Websocket实现的消息流
 * @author guyi
 */
@Slf4j
public class WebSocketMessageStream implements MessageStream<Boolean> {

    @Resource
    private TopicHandlerFactory factory;
    @Resource
    private WebSocketConfiguration configuration;
    @Resource
    private WebSocketServerExecutors executors;
    @Resource
    private MessageStreamWorker worker;

    // 表示是否需要断开重连
    private boolean run;
    // 重连任务
    private ScheduledFuture<?> future;
    // Websocket连接
    private WebsocketConnection connection;

    // 创建连接
    private WebsocketConnection createConnection(MessageReceiver receiver) throws URISyntaxException {
        return new WebsocketConnection(
                new URI(this.executors.replace(this.configuration.getServer())),
                origin -> {
                    byte[] bytes = origin.getBytes(StandardCharsets.UTF_8);
                    receiver.accept(this.factory.get().getTopic(bytes), bytes, null);
                },
                e -> {
                    // 打开断线重连
                    this.run = true;
                    Optional.ofNullable(this.future).ifPresent(future -> future.cancel(true));

                    log.info("Websocket连接建立");
                },
                () -> this.reconnect(receiver),
                e -> {
                    log.error("WebSocket连接异常", e);
                    this.reconnect(receiver);
                }
        );
    }

    // 重连
    private void reconnect(MessageReceiver receiver){
        if (this.run){
            // 关闭重连, 防止重复建立重连任务
            this.run = false;

            this.future = this.worker.scheduleWithFixedDelay(() -> {
                this.connect(receiver);
            }, 0, 10, TimeUnit.SECONDS);
        }
    }

    // 连接服务器
    @SneakyThrows
    private void connect(MessageReceiver receiver) {
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
        // 关闭断开重连
        this.run = false;

        Optional.ofNullable(this.connection)
                .ifPresent(WebSocketClient::close);
    }

    @Override
    @SneakyThrows
    public void open(MessageReceiver receiver) {
        this.connect(receiver);
    }

    @Override
    public Optional<Boolean> publish(String topic, byte[] bytes, Map<Class<? extends AttachKey>,Object> attach) {
        if (!this.connection.isOpen()){
            throw new ConnectionNotReadyException();
        }
        this.connection.send(this.factory.get().setTopic(topic,bytes));
        return Optional.of(true);
    }

}

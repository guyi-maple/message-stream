package tech.guyi.component.message.stream.websocket;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.api.stream.entry.Message;
import tech.guyi.component.message.stream.websocket.connection.WebsocketConnection;
import tech.guyi.component.message.stream.websocket.exception.ConnectionNotReadyException;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 基于Websocket实现的消息流
 * @author guyi
 * @date 2021/1/18 14:54
 */
public class WebSocketMessageStream implements MessageStream {

    @Resource
    private TopicHandlerFactory factory;
    @Resource
    private WebSocketConfiguration configuration;

    // Websocket连接
    private WebsocketConnection connection;

    // 连接服务器
    private void connect(){
        if (!connection.isOpen()) {
            if (connection.getReadyState() == ReadyState.NOT_YET_CONNECTED) {
                connection.connect();
            } else if (connection.getReadyState() == ReadyState.CLOSING || connection.getReadyState() == ReadyState.CLOSING) {
                connection.reconnect();
            }
        }
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
        this.connection = new WebsocketConnection(
                new URI(configuration.getServer()),
                origin -> {
                    byte[] bytes = origin.getBytes(StandardCharsets.UTF_8);
                    receiver.accept(new Message(this.factory.get().getTopic(bytes), bytes));
                },
                e -> System.out.println("连接建立"),null,null
        );
        this.connect();
    }

    @Override
    public void publish(Message message) {
        if (!this.connection.isOpen()){
            throw new ConnectionNotReadyException();
        }
        this.connection.send(this.factory.get().setTopic(message.getTopic(),message.getBytes()));
    }

}

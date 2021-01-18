package tech.guyi.component.message.stream.websocket;

import lombok.NonNull;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.InitializingBean;
import tech.guyi.component.message.stream.api.MessageStream;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;
import tech.guyi.component.message.stream.websocket.connection.WebsocketConnection;
import tech.guyi.component.message.stream.websocket.consumer.ConsumerRepository;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 基于Websocket实现的消息流
 * @author guyi
 * @date 2021/1/18 14:54
 */
public class WebSocketMessageStream implements MessageStream, InitializingBean {

    @Resource
    private ConsumerRepository repository;
    @Resource
    private TopicHandlerFactory factory;
    @Resource
    private WebSocketConfiguration configuration;

    // Websocket连接
    private WebsocketConnection connection;
    // 连接任务
    private ScheduledFuture<?> future;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.connection = new WebsocketConnection(
                new URI(configuration.getServer()),
                this::receive,
                e -> System.out.println("连接建立"),
                () -> {
                    System.out.println("连接断开");
                    // 重连
//                    connection.reconnect();
                },
                e -> {
                    System.out.println("连接异常");
                    e.printStackTrace();
                }
        );
        this.connect();
    }

    private void connect(){
        if (!connection.isOpen()) {
            if (connection.getReadyState() == ReadyState.NOT_YET_CONNECTED) {
                connection.connect();
            } else if (connection.getReadyState() == ReadyState.CLOSING || connection.getReadyState() == ReadyState.CLOSING) {
                connection.reconnect();
            }
        }
    }

    private void receive(String origin){
        System.out.println("收到消息 " + origin);
        Message message = new Message();
        message.setTopic(this.factory.get().getTopic(origin));
        message.setContent(origin.getBytes(StandardCharsets.UTF_8));
        message.setStream(this.getName());
        message.setAddress(this.configuration.getServer());
        this.repository.receive(message);
    }

    @Override
    public @NonNull String getName() {
        return "websocket";
    }

    @Override
    public void publish(Message message) {
        String json = new String(message.getContent());
        json = this.factory.get().setTopic(message.getTopic(),json);
        this.connection.send(json);
    }

    @Override
    public void register(MessageConsumerEntry consumer) {
        this.repository.add(consumer);
    }

    @Override
    public void unregister(String topic) {
        this.repository.remove(topic);
    }

    @Override
    public void close() {
        Optional.ofNullable(this.connection)
                .ifPresent(WebSocketClient::close);
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        String url = "ws://121.36.65.0:8844/messaging?:X_Access_Token=136a06da8c6376ca3183a5aa160680cb";
        WebSocketClient client = new WebSocketClient(new URI(url)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("打开连接");
            }

            @Override
            public void onMessage(String s) {
                System.out.println("收到消息：" + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("连接关闭");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("连接出错");
                e.printStackTrace();
            }
        };
        client.connectBlocking();

        if (client.isOpen()){
            client.send("{\"type\":\"sub\",\"topic\":\"/rule-engine/device/alarm/**\",\"id\":\"request-id\"}");
            System.out.println("发送消息 {\"type\":\"sub\",\"topic\":\"/device/*/message/*\",\"id\":\"request-id\"}");
            TimeUnit.MINUTES.sleep(5);
        }
    }

}

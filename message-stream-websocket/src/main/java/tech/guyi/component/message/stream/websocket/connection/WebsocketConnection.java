package tech.guyi.component.message.stream.websocket.connection;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Websocket连接
 * @author guyi
 */
public class WebsocketConnection extends WebSocketClient {

    /**
     * 消息到达回调
     */
    private final Consumer<String> onReceive;

    /**
     * 连接建立回调
     */
    private final Consumer<ServerHandshake> onOpen;

    /**
     * 连接关闭回调
     */
    private final Runnable onClose;

    /**
     * 连接异常回调
     */
    private final Consumer<Exception> onError;

    public WebsocketConnection(URI server, Consumer<String> onReceive, Consumer<ServerHandshake> onOpen, Runnable onClose, Consumer<Exception> onError) {
        super(server);
        this.onReceive = onReceive;
        this.onOpen = onOpen;
        this.onClose = onClose;
        this.onError = onError;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Optional.ofNullable(this.onOpen).ifPresent(on -> on.accept(serverHandshake));
    }

    @Override
    public void onMessage(String message) {
        Optional.ofNullable(this.onReceive).ifPresent(on -> on.accept(message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Optional.ofNullable(this.onClose).ifPresent(Runnable::run);
    }

    @Override
    public void onError(Exception e) {
        Optional.ofNullable(this.onError).ifPresent(on -> on.accept(e));
    }

}

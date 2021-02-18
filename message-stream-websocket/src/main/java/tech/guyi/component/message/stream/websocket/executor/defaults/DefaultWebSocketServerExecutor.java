package tech.guyi.component.message.stream.websocket.executor.defaults;

import tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutor;

/**
 * 默认表达式替换, 所有表达式都返回null
 * @author guyi
 */
public class DefaultWebSocketServerExecutor implements WebSocketServerExecutor {

    @Override
    public String execute(String ex) {
        return null;
    }
}

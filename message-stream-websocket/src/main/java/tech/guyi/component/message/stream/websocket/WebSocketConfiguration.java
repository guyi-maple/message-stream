package tech.guyi.component.message.stream.websocket;

import lombok.Data;

/**
 * 配置
 * @author guyi
 */
@Data
public class WebSocketConfiguration {

    /**
     * 是否启用
     */
    private boolean enable = true;

    /**
     * <p>Websocket连接地址.</p>
     * <p>允许使用表达式 {name}.</p>
     * <p>使用 WebSocketServerExecutor 可对表达式进行替换.</p>
     * @see tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutor
     */
    private String server;

    /**
     * 要启用的Topic处理器名称.
     */
    private String topicHandlerName = "default-json";

}

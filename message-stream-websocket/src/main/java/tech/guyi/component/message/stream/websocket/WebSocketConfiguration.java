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
     * Websocket连接地址.
     * 允许使用表达式 {name}.
     * 使用 tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutor 可对表达式进行替换
     */
    private String server;
    /**
     * 要启用的Topic处理器名称.
     */
    private String topicHandlerName = "default-json";

}

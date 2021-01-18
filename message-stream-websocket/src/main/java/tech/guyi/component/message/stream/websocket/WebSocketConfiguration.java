package tech.guyi.component.message.stream.websocket;

import lombok.Data;

/**
 * 配置
 * @author guyi
 * @date 2021/1/18 14:55
 */
@Data
public class WebSocketConfiguration {

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * Websocket连接地址 <br />
     */
    private String server;
    /**
     * 要启用的Topic处理器名称 <br />
     */
    private String topicHandlerName = "default-json";

}

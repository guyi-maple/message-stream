package tech.guyi.component.message.stream.websocket.getter;

import tech.guyi.component.message.stream.websocket.WebSocketConfiguration;

/**
 * Websocket连接地址获取器
 * @author guyi
 * @date 2021/1/19 11:29
 */
public interface WebSocketServerAddressGetter {

    /**
     * 获取连接地址
     * @param configuration 默认配置
     * @return
     */
    String get(WebSocketConfiguration configuration);

}

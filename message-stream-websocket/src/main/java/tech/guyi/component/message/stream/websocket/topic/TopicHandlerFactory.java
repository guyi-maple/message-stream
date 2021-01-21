package tech.guyi.component.message.stream.websocket.topic;

import tech.guyi.component.message.stream.websocket.WebSocketConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Topic处理器工厂
 * @author guyi
 */
public class TopicHandlerFactory {

    @Resource
    private WebSocketConfiguration configuration;
    @Resource
    private List<TopicHandler> handlers;
    @Resource
    private JsonMessageTopicHandler defaultHandler;

    /**
     * 获取处理器.
     * 如果不存在指定的处理器, 则返回默认的JSON格式处理器
     * @return 处理器
     */
    public TopicHandler get(){
        return this.handlers.stream()
                .filter(handler -> handler.getName().equals(this.configuration.getTopicHandlerName()))
                .findFirst()
                .orElse(this.defaultHandler);
    }

}

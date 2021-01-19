package tech.guyi.component.message.stream.websocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.websocket.topic.JsonMessageTopicHandler;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

/**
 * 自动装配
 * @author guyi
 * @date 2021/1/18 14:53
 */
@Configuration
@ConditionalOnProperty(value = "message.stream.websocket.enable", havingValue = "true", matchIfMissing = true)
public class WebSocketAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.websocket")
    public WebSocketConfiguration webSocketConfiguration(){
        return new WebSocketConfiguration();
    }

    @Bean
    public WebSocketMessageStream webSocketMessageStream(){
        return new WebSocketMessageStream();
    }

    @Bean
    public JsonMessageTopicHandler jsonMessageTopicHandler(){
        return new JsonMessageTopicHandler();
    }

    @Bean
    public TopicHandlerFactory topicHandlerFactory(){
        return new TopicHandlerFactory();
    }

}

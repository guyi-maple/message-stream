package tech.guyi.component.message.stream.websocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutor;
import tech.guyi.component.message.stream.websocket.executor.WebSocketServerExecutors;
import tech.guyi.component.message.stream.websocket.executor.defaults.DefaultWebSocketServerExecutor;
import tech.guyi.component.message.stream.websocket.topic.JsonMessageTopicHandler;
import tech.guyi.component.message.stream.websocket.topic.TopicHandlerFactory;

/**
 * 自动装配
 * @author guyi
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

    @Bean
    @ConditionalOnBean(WebSocketServerExecutor.class)
    public DefaultWebSocketServerExecutor defaultWebSocketServerExecutor(){
        return new DefaultWebSocketServerExecutor();
    }

    @Bean
    public WebSocketServerExecutors webSocketServerExecutors(){
        return new WebSocketServerExecutors();
    }

}

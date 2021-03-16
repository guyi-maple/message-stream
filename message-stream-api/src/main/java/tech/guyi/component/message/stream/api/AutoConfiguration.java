package tech.guyi.component.message.stream.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.api.consumer.ConsumerAutoRegister;
import tech.guyi.component.message.stream.api.consumer.MessageConsumers;
import tech.guyi.component.message.stream.api.converter.MessageTypeConverters;
import tech.guyi.component.message.stream.api.converter.defaults.StringMessageTypeConverter;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.utils.AntPathMatchers;
import tech.guyi.component.message.stream.api.stream.publish.MessageStreamPublisher;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.api.worker.defaults.DefaultMessageStreamWorker;

/**
 * 自动装配
 * @author guyi
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public MessageStreams messageStreamRepository(){
        return new MessageStreams();
    }

    @Bean
    public ConsumerAutoRegister autoRegister(){
        return new ConsumerAutoRegister();
    }

    @Bean
    public OnApplicationContextClose onApplicationContextClose(){
        return new OnApplicationContextClose();
    }

    @Bean
    public MessageStreamHookRunner messageStreamHookRunner(){
        return new MessageStreamHookRunner();
    }

    @Bean
    public MessageConsumers messageConsumers(){
        return new MessageConsumers();
    }

    @Bean
    public MessageTypeConverters messageTypeConverters(){
        return new MessageTypeConverters();
    }

    @Bean
    public StringMessageTypeConverter stringMessageTypeConverter(){
        return new StringMessageTypeConverter();
    }

    @Bean
    public AntPathMatchers antPathMatchers(){
        return new AntPathMatchers();
    }

    @Bean
    public MessageStreamPublisher messagePublisher(){
        return new MessageStreamPublisher();
    }

    @Bean
    @ConditionalOnMissingBean(MessageStreamWorker.class)
    public DefaultMessageStreamWorker defaultMessageStreamWorker(){
        return new DefaultMessageStreamWorker(20);
    }

}

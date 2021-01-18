package tech.guyi.component.message.stream.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.api.hook.MessageStreamHookRunner;

/**
 * 自动装配
 * @author guyi
 * @date 2021/1/15 22:50
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public MessageStreamRepository messageStreamRepository(){
        return new MessageStreamRepository();
    }

    @Bean
    public AutoRegister autoRegister(){
        return new AutoRegister();
    }

    @Bean
    public OnApplicationContextClose onApplicationContextClose(){
        return new OnApplicationContextClose();
    }

    @Bean
    public MessageStreamHookRunner messageStreamHookRunner(){
        return new MessageStreamHookRunner();
    }

}

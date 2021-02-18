package tech.guyi.component.message.stream.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 * @author guyi
 */
@Configuration
@ConditionalOnProperty(value = "message.stream.redis.enable", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.redis")
    public RedisConfiguration redisConfiguration(){
        return new RedisConfiguration();
    }

    @Bean
    public RedisMessageStream redisMessageStream(){
        return new RedisMessageStream();
    }

}

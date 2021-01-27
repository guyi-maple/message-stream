package tech.guyi.component.message.stream.rocketmq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqAliyunConfiguration;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqConfiguration;
import tech.guyi.component.message.stream.rocketmq.creatoe.AliyunRocketmqCreator;
import tech.guyi.component.message.stream.rocketmq.creatoe.DefaultRocketmqCreator;

/**
 * 自动装配
 * @author guyi
 */
@Configuration
@ConditionalOnProperty(value = "tech.guyi.message.stream.rocketmq.enable", havingValue = "true", matchIfMissing = true)
public class RocketmqAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "tech.guyi.message.stream.rocketmq")
    public RocketmqConfiguration rocketmqConfiguration(){
        return new RocketmqConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "tech.guyi.message.stream.rocketmq.aliyun")
    public RocketmqAliyunConfiguration rocketmqAliyunConfiguration(){
        return new RocketmqAliyunConfiguration();
    }

    @Bean
    public RocketmqMessageStream rocketmqMessageStream(){
        return new RocketmqMessageStream();
    }

    @Bean
    @ConditionalOnMissingBean(DefaultRocketmqCreator.class)
    public DefaultRocketmqCreator defaultRocketmqCreator(){
        return new DefaultRocketmqCreator();
    }

    @Bean
    @ConditionalOnMissingBean(AliyunRocketmqCreator.class)
    public AliyunRocketmqCreator aliyunRocketmqCreator(){
        return new AliyunRocketmqCreator();
    }

}

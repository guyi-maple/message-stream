package tech.guyi.component.message.stream.rabbitmq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guyi
 */
@Configuration
public class RabbitmqAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.rabbitmq")
    public RabbitmqConfiguration rabbitmqConfiguration(){
        return new RabbitmqConfiguration();
    }

    @Bean
    @ConditionalOnProperty(value = "message.stream.rabbitmq.enable", havingValue = "true", matchIfMissing = true)
    public RabbitmqMessageStream rabbitmqMessageStream(){
        return new RabbitmqMessageStream();
    }

}

package tech.guyi.component.message.stream.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.kafka.configuration.KafkaConfiguration;
import tech.guyi.component.message.stream.kafka.configuration.KafkaConsumerConfiguration;
import tech.guyi.component.message.stream.kafka.configuration.KafkaProducerConfiguration;

/**
 * 自动装配
 * @author guyi
 * @date 2021/1/19 16:06
 */
@Configuration
@ConditionalOnProperty(value = "message.stream.kafka.enable", havingValue = "true", matchIfMissing = true)
public class KafkaAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.kafka")
    public KafkaConfiguration kafkaConfiguration(){
        return new KafkaConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "message.stream.kafka.producer")
    public KafkaProducerConfiguration kafkaProducerConfiguration(){
        return new KafkaProducerConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "message.stream.kafka.consumer")
    public KafkaConsumerConfiguration kafkaConsumerConfiguration(){
        return new KafkaConsumerConfiguration();
    }

    @Bean
    public KafkaMessageStream kafkaMessageStream(){
        return new KafkaMessageStream();
    }

}

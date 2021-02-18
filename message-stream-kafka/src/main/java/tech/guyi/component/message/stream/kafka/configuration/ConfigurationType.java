package tech.guyi.component.message.stream.kafka.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * 配置类型
 * @author guyi
 */
@Getter
@AllArgsConstructor
public enum ConfigurationType {

    /**
     * 生产者配置
     */
    PRODUCER(KafkaConfiguration::getProducer),

    /**
     * 消费者配置
     */
    CONSUMER(KafkaConfiguration::getConsumer);

    private final Function<KafkaConfiguration, ConfigurationInterface> value;

}

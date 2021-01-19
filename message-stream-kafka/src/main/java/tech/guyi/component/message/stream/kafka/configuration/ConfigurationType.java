package tech.guyi.component.message.stream.kafka.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author guyi
 * @date 2021/1/19 16:18
 */
@Getter
@AllArgsConstructor
public enum ConfigurationType {

    PRODUCER(KafkaConfiguration::getProducer),
    CONSUMER(KafkaConfiguration::getConsumer);

    private final Function<KafkaConfiguration, ConfigurationInterface> value;

}

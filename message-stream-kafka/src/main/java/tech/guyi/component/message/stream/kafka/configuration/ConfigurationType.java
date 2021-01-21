package tech.guyi.component.message.stream.kafka.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author guyi
 */
@Getter
@AllArgsConstructor
public enum ConfigurationType {

    PRODUCER(KafkaConfiguration::getProducer),
    CONSUMER(KafkaConfiguration::getConsumer);

    private final Function<KafkaConfiguration, ConfigurationInterface> value;

}

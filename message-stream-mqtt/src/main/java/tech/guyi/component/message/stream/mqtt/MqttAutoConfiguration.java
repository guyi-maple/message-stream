package tech.guyi.component.message.stream.mqtt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guyi
 * @date 2021/4/23 10:47
 */
@Configuration
@ConditionalOnProperty(value = "message.stream.mqtt.enable", havingValue = "true", matchIfMissing = true)
public class MqttAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.mqtt")
    public MqttConfiguration mqttConfiguration() {
        return new MqttConfiguration();
    }

    @Bean
    public MqttMessageStream mqttMessageStream() {
        return new MqttMessageStream();
    }

}

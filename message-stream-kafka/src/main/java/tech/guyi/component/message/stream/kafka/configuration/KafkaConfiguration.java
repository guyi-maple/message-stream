package tech.guyi.component.message.stream.kafka.configuration;

import lombok.Data;
import lombok.Getter;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author guyi
 * @date 2021/1/19 16:06
 */
@Data
public class KafkaConfiguration {

    @Getter
    @Resource
    private KafkaProducerConfiguration producer;
    @Getter
    @Resource
    private KafkaConsumerConfiguration consumer;

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 服务器地址
     */
    private String bootstrapServers;

    /**
     * 获取服务器地址 <br />
     * 如果子配置不存在该配置项, 则使用基础配置中的值
     * @param type 类型
     * @return 服务器地址
     */
    public String getBootstrapServers(ConfigurationType type){
        return Optional.ofNullable(type.getValue().apply(this))
                .map(ConfigurationInterface::getBootstrapServers)
                .orElse(this.bootstrapServers);
    }

}

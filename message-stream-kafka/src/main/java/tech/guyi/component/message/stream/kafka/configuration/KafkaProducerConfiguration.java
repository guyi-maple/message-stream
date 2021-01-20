package tech.guyi.component.message.stream.kafka.configuration;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author guyi
 * @date 2021/1/19 16:11
 */
@Data
public class KafkaProducerConfiguration implements ConfigurationInterface {

    /**
     * 服务器地址
     */
    private String bootstrapServers;

    /**
     * topic <br />
     * 默认值 message.stream
     */
    private String topic = "message.stream";

    /**
     * 等待响应
     */
    private String acks = "all";

    /**
     * 重试次数 <br />
     * 默认不重试
     */
    private int retries = 0;

    /**
     * 消息合并
     */
    private int batchSize = 6384;

    /**
     * 键序列化
     */
    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * 值序列化
     */
    private String valueSerializer = "org.apache.kafka.common.serialization.ByteArraySerializer";

}

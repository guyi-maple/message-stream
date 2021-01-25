package tech.guyi.component.message.stream.kafka.configuration;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author guyi
 */
@Data
public class KafkaProducerConfiguration implements ConfigurationInterface {

    /**
     * 服务器地址
     */
    private String bootstrapServers;

    /**
     * topic
     */
    private String topic = "message.stream";

    /**
     * 等待响应
     */
    private String acks = "all";

    /**
     * <p>重试次数.</p>
     * <p>为零表示不重试.</p>
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

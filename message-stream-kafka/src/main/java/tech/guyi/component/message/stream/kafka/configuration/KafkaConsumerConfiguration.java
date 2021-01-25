package tech.guyi.component.message.stream.kafka.configuration;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author guyi
 */
@Data
public class KafkaConsumerConfiguration implements ConfigurationInterface {

    /**
     * 服务器地址
     */
    private String bootstrapServers;

    /**
     * 是否启用消费者
     */
    private boolean enable = true;

    /**
     * topic
     */
    private List<String> topic = Collections.singletonList("message.stream");

    /**
     * 分组
     */
    private String groupId = "message.stream.kafka.consumer";

    /**
     * 是否启用自动提交
     */
    private boolean autoCommit = true;

    /**
     * <p>单次拉取最大延迟.</p>
     * <p>单位毫秒.</p>
     */
    private int interval = 300000;

    /**
     * <p>心跳时间.</p>
     * <p>单位毫秒.</p>
     * <p>请不要超过timeout值的三分之一.</p>
     */
    private int heartbeat = 3000;

    /**
     * <p>超时时间.</p>
     * <p>单位毫秒.</p>
     */
    private int timeout = 10000;

    /**
     * 一次拉取的最大个数
     */
    private int maxRecords = 1000;

    /**
     * <p>消费规则.</p>
     * <p>枚举 earliest, latest, none.</p>
     */
    private String reset = "earliest";

    /**
     * 键反序列化
     */
    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    /**
     * 值反序列化
     */
    private String valueDeserializer = "org.apache.kafka.common.serialization.ByteArrayDeserializer";

}

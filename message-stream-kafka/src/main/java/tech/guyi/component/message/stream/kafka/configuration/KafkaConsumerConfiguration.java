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
     * 是否启用消费者.
     * 默认启用
     */
    private boolean enable = true;

    /**
     * topic.
     * 默认值 [message.stream]
     */
    private List<String> topic = Collections.singletonList("message.stream");

    /**
     * 分组.
     * 默认 message.stream.kafka.consumer
     */
    private String groupId = "message.stream.kafka.consumer";

    /**
     * 是否启用自动提交.
     * 默认启用
     */
    private boolean autoCommit = true;

    /**
     * 单次拉取最大延迟.
     * 单位毫秒.
     * 默认300000毫秒
     */
    private int interval = 300000;

    /**
     * 心跳时间.
     * 单位毫秒.
     * 默认3000毫秒.
     * 请不要超过timeout值的三分之一
     */
    private int heartbeat = 3000;

    /**
     * 超时时间.
     * 单位毫秒.
     * 默认3000毫秒
     */
    private int timeout = 10000;

    /**
     * 一次拉取的最大个数.
     * 默认1000
     */
    private int maxRecords = 1000;

    /**
     * 消费规则.
     * earliest, latest, none.
     * 默认 earliest
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

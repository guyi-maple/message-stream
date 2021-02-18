package tech.guyi.component.message.stream.rocketmq.configuration;

import lombok.Data;

/**
 * Rocketmq配置
 * @author guyi
 */
@Data
public class RocketmqConfiguration {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 命名服务器地址
     */
    private String nameServer;

    /**
     * 全局分组ID
     */
    private String groupId;

    /**
     * <p>默认Topic</p>
     * <p>此字段是Rocketmq中的Topic概念, 与消息消费者中的Topic并不是同一个东西</p>
     * <p>消息消费者中的Topic会被转换为Rocketmq中的Tag</p>
     */
    private String topic;

}

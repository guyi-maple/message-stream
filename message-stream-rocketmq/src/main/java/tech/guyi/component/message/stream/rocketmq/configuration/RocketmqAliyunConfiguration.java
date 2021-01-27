package tech.guyi.component.message.stream.rocketmq.configuration;

import lombok.Data;

/**
 * 阿里云Rocketmq配置
 * @author guyi
 */
@Data
public class RocketmqAliyunConfiguration {

    /**
     * 是否启用阿里云环境
     */
    private boolean enable;

    /**
     * 是否启用消息轨迹功能
     */
    private boolean cloud;

    /**
     * 阿里云AccessKey
     */
    private String AccessKey;

    /**
     * 阿里云SecretKey
     */
    private String SecretKey;

}

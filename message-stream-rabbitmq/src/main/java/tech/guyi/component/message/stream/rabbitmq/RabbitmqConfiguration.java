package tech.guyi.component.message.stream.rabbitmq;

import lombok.Data;

/**
 * Rabbitmq配置
 * @author guyi
 * @date 2021/1/16 12:36
 */
@Data
public class RabbitmqConfiguration {

    /**
     * 是否启用
     */
    private boolean enable = true;
    private String username = "guest";
    private String password = "guest";
    private String host = "127.0.0.1";
    private int port = 5672;
    private String virtualHost = "/";
    private String queue = "message.stream";
    private String exchange = "amq.topic";

}

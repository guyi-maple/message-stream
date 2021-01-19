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
    /**
     * 用户名
     */
    private String username = "guest";
    /**
     * 密码
     */
    private String password = "guest";
    /**
     * 服务器地址
     */
    private String host = "127.0.0.1";
    /**
     * 端口号
     */
    private int port = 5672;
    /**
     * 虚拟主机
     */
    private String virtualHost = "/";
    /**
     * 消息队列名称
     */
    private String queue = "message.stream";
    /**
     * 路由名称
     */
    private String exchange = "amq.topic";

}

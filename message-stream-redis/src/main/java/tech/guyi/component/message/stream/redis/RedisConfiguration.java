package tech.guyi.component.message.stream.redis;

import lombok.Data;

/**
 * @author guyi
 * @date 2021/1/19 12:52
 */
@Data
public class RedisConfiguration {

    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * Redis服务地址
     */
    private String host = "127.0.0.1";
    /**
     * Redis服务端口
     */
    private int port = 6379;
    /**
     * Redis数据库
     */
    private int database = 0;

}

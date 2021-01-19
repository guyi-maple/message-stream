package tech.guyi.component.message.stream.kafka.configuration;

import java.util.List;

/**
 * @author guyi
 * @date 2021/1/19 16:21
 */
public interface ConfigurationInterface {

    /***
     * 获取服务器地址
     * @return 服务器地址
     */
    String getBootstrapServers();

}

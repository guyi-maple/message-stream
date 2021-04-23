package tech.guyi.component.message.stream.mqtt;

import lombok.Data;

/**
 * @author guyi
 * @date 2021/4/23 10:22
 */
@Data
public class MqttConfiguration {

    private boolean enable = true;

    private String server = "tcp://127.0.0.1:1883";
    private String clientId = "message-stream";

    private String username;
    private String password;

    private Integer qos = 0;
    private Integer retryDelay = 5;

    private boolean retained = false;

}

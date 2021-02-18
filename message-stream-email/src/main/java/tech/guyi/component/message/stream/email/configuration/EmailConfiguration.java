package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * @author guyi
 */
@Data
public class EmailConfiguration {

    /**
     * 是否启用此消息流
     */
    private boolean enable = true;
    /**
     * SMTP服务器地址
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 端口号
     */
    private int port = 25;

}

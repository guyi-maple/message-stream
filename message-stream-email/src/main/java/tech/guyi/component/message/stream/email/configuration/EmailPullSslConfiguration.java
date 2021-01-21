package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * 邮件拉取SSL配置
 * @author guyi
 */
@Data
public class EmailPullSslConfiguration {

    /**
     * 是否启用SSL
     */
    private boolean enable;
    /**
     * SSL端口
     */
    private int port = 993;

}

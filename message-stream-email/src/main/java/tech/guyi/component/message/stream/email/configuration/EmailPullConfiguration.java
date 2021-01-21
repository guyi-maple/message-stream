package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * 邮件拉取配置
 * @author guyi
 */
@Data
public class EmailPullConfiguration {

    /**
     * 拉取时间间隔.
     * 单位为分钟
     */
    private int delay = 1;
    /**
     * 邮件拉取协议.
     * 默认 imap
     */
    private String protocol = "imap";
    /**
     * 邮件服务器地址
     */
    private String host;
    /**
     * 邮件服务器端口.
     * 默认 143
     */
    private int port = 143;

}

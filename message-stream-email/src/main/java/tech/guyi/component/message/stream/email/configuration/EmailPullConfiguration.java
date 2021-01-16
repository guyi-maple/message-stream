package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * 邮件拉取配置
 * @author guyi
 * @date 2021/1/16 18:34
 */
@Data
public class EmailPullConfiguration {

    /**
     * 拉取时间间隔 <br />
     * 单位为分钟
     */
    private int delay = 1;
    /**
     * 邮件拉取协议 <br />
     * 默认 imap
     */
    private String protocol = "imap";
    /**
     * 邮件服务器地址
     */
    private String host;
    /**
     * 邮件服务器端口 <br />
     * 默认 143
     */
    private int port = 143;

}

package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * 邮件提取配置
 * @author guyi
 * @date 2021/1/16 21:09
 */
@Data
public class EmailExtractorConfiguration {

    /**
     * Topic模板 <br />
     * 当收到邮件时, 将邮件标题填入%s所在的位置, 并发布消息到消费者 <br />
     */
    private String template = "/email/%s";
    /**
     * 当发送邮件时, 使用此正则表达式提取出邮件标题
     */
    private String extract = "^/email/(.*)$";
    /**
     * 默认的邮件标题 <br />
     * 当标题提取表达式没有提取到标题时, 使用此字段
     */
    private String defaultTitle = "来自邮件服务器";

}

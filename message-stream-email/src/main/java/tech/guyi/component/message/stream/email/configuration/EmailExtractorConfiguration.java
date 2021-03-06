package tech.guyi.component.message.stream.email.configuration;

import lombok.Data;

/**
 * 邮件提取配置
 * @author guyi
 */
@Data
public class EmailExtractorConfiguration {

    /**
     * <p>Topic模板.</p>
     * <p>当收到邮件时, 将邮件标题填入%s所在的位置, 并发布消息到消费者.</p>
     */
    private String template = "/email/%s";

    /**
     * 当发送邮件时, 使用此正则表达式提取出邮件标题
     */
    private String extract = "^/email/(.*)$";
    
    /**
     * <p>默认的邮件标题.</p>
     * <p>当标题提取表达式没有提取到标题时, 使用此字段</p>
     */
    private String defaultTitle = "来自邮件服务器";

}

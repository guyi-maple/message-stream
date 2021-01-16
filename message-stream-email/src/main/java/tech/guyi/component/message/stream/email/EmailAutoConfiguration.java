package tech.guyi.component.message.stream.email;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.component.message.stream.email.configuration.EmailConfiguration;
import tech.guyi.component.message.stream.email.configuration.EmailExtractorConfiguration;
import tech.guyi.component.message.stream.email.configuration.EmailPullConfiguration;
import tech.guyi.component.message.stream.email.configuration.EmailPullSslConfiguration;
import tech.guyi.component.message.stream.email.extract.TitleExtractor;

/**
 * 自动配置
 * @author guyi
 * @date 2021/1/16 15:19
 */
@Configuration
@ConditionalOnProperty(value = "message.stream.email.enable", havingValue = "true", matchIfMissing = true)
public class EmailAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "message.stream.email")
    public EmailConfiguration emailConfiguration(){
        return new EmailConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "message.stream.email.extract")
    public EmailExtractorConfiguration emailExtractorConfiguration(){
        return new EmailExtractorConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "message.stream.email.pull")
    public EmailPullConfiguration emailPullConfiguration(){
        return new EmailPullConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "message.stream.email.pull.ssl")
    public EmailPullSslConfiguration emailPullSslConfiguration(){
        return new EmailPullSslConfiguration();
    }

    @Bean
    public EmailMessageStream emailMessageStream(){
        return new EmailMessageStream();
    }

    @Bean
    public EmailService emailService(){
        return new EmailService();
    }

    @Bean
    public TitleExtractor titleExtractor(){
        return new TitleExtractor();
    }

}

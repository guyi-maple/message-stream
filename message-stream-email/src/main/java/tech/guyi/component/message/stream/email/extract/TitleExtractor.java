package tech.guyi.component.message.stream.email.extract;

import lombok.Data;
import tech.guyi.component.message.stream.email.configuration.EmailExtractorConfiguration;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件标题提取器
 * @author guyi
 */
@Data
public class TitleExtractor {

    @Resource
    private EmailExtractorConfiguration extract;

    /**
     * 提取邮件标题
     * @param topic topic
     * @return 邮件标题
     */
    public String getTitle(String topic){
        Matcher matcher = Pattern.compile(extract.getExtract()).matcher(topic);
        if (matcher.find()){
            return matcher.group(1);
        }
        return extract.getDefaultTitle();
    }

    /**
     * 通过邮件标题生成Topic
     * @param title 邮件标题
     * @return Topic
     */
    public String getTopic(String title){
        return String.format(extract.getTemplate(),title);
    }

}

package tech.guyi.component.message.stream.email.entry;

import lombok.Data;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email消息实体
 * @author guyi
 * @date 2021/1/16 19:28
 */
@Data
public class EmailMessage {

    /**
     * 邮件主题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件发送者
     */
    private String source;

    private static String getContent(MimeMessage message) throws MessagingException, IOException {
        if (message.getContentType().startsWith("text/")){
            return message.getContent().toString();
        }else{
            Multipart part = (Multipart) message.getContent();
            return part.getBodyPart(0).getContent().toString();
        }
    }

    private static String getAddress(Address address){
        Pattern pattern = Pattern.compile("^.*<(.*)>$");
        Matcher matcher = pattern.matcher(address.toString());
        if (matcher.find()){
            return matcher.group(1);
        }
        return address.toString();
    }

    public static EmailMessage from(MimeMessage message) throws MessagingException, IOException {
        EmailMessage email = new EmailMessage();
        email.setTitle(message.getSubject());
        email.setContent(getContent(message));
        email.setSource(message.getSender().getType());
        email.setSource(getAddress(message.getSender()));
        return email;
    }

}

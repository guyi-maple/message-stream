package tech.guyi.component.message.stream.email;

import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;
import tech.guyi.component.message.stream.email.configuration.EmailConfiguration;
import tech.guyi.component.message.stream.email.configuration.EmailPullConfiguration;
import tech.guyi.component.message.stream.email.configuration.EmailPullSslConfiguration;
import tech.guyi.component.message.stream.email.entry.EmailMessage;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 邮件服务
 * @author guyi
 */
public class EmailService implements InitializingBean {

    private final List<Consumer<EmailMessage>> consumers = new LinkedList<>();

    @Resource
    private EmailConfiguration email;
    @Resource
    private EmailPullConfiguration pull;
    @Resource
    private EmailPullSslConfiguration ssl;
    @Resource
    private MessageStreamWorker worker;

    private Future<?> future;
    @Override
    public void afterPropertiesSet() {
        if (email.isEnable()){
            this.future = this.worker.scheduleWithFixedDelay(
                    this::pullEmail,
                    0,
                    pull.getDelay(),
                    TimeUnit.MINUTES
            );
        }
    }

    // 拉取服务器收件箱中的邮件.
    // 件拉取完成后将会被置为已读状态.
    @SneakyThrows
    private void pullEmail(){
        try{
            Session session = this.getSession();
            Store store = this.getStore(session);
            Folder folder = this.getFolder(store);

            // 邮件缓存集合
            List<Message> messages = new LinkedList<>();
            // 获取邮件总数
            int length = folder.getMessageCount();
            // 获取未读邮件总数
            int unread = folder.getUnreadMessageCount();
            for (int i = 0; i < unread; i++) {
                // 获取未读邮件并推送
                MimeMessage message = (MimeMessage) folder.getMessage(length--);
                message.setFlag(Flags.Flag.SEEN, true);
                EmailMessage email = EmailMessage.from(message);
                for (Consumer<EmailMessage> consumer : this.consumers) {
                    consumer.accept(email);
                }

                //将邮件加入缓存集合
                messages.add(message);
            }

            // 将邮件缓存集合中的邮件置为已读
            folder.setFlags(messages.toArray(new Message[0]), new Flags(Flags.Flag.SEEN), true);

            // 资源清理
            folder.close(false);
            store.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 替换邮件协议
    private String replaceProtocol(String key,String protocol){
        return String.format(key,protocol);
    }

    // 获取邮件拉取Session
    private Session getSession(){
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", pull.getProtocol());
        props.setProperty(replaceProtocol("mail.%s.host", pull.getProtocol()), email.getHost());
        props.setProperty(replaceProtocol("mail.%s.port", pull.getProtocol()), String.valueOf(email.getPort()));

        if (ssl.isEnable()){
            props.setProperty(replaceProtocol("mail.%s.socketFactory.class",pull.getProtocol()), "javax.net.ssl.SSLSocketFactory");
            props.setProperty(replaceProtocol("mail.%s.socketFactory.fallback", pull.getProtocol()), "false");
            props.setProperty(replaceProtocol("mail.%s.starttls.enable", pull.getProtocol()),"true");
            props.setProperty(replaceProtocol("mail.%s.socketFactory.port", pull.getProtocol()), String.valueOf(ssl.getPort()));
        }

        return Session.getDefaultInstance(props);
    }

    // 获取消息存储
    private Store getStore(Session session) throws NoSuchProviderException {
        return session.getStore(pull.getProtocol());
    }

    // 获取收件箱
    private Folder getFolder(Store store) throws MessagingException {
        store.connect(email.getUsername(),email.getPassword());
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    /**
     * 添加邮件消息接收者
     * @param receiver 邮件消息接收者
     * @return this
     */
    public EmailService onEmail(Consumer<EmailMessage> receiver){
        this.consumers.add(receiver);
        return this;
    }

    /**
     * 发送邮件
     * @param to 接收者
     * @param title 标题
     * @param content 正文
     */
    @SneakyThrows
    public void send(String to,String title, String content){
        Properties props = new Properties();
        props.put("mail.smtp.host", email.getHost());
        props.put("mail.smtp.port", String.valueOf(email.getPort()));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getUsername(), email.getPassword());
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email.getUsername()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title, StandardCharsets.UTF_8.name());
        message.setText(content, StandardCharsets.UTF_8.name());
        Transport.send(message);
    }

    /**
     * 关闭服务
     */
    public void close(){
        if (this.future != null){
            this.future.cancel(true);
        }
        this.consumers.clear();
    }

}

package tech.guyi.component.message.stream.email;

import lombok.NonNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import tech.guyi.component.message.stream.api.MessageStream;
import tech.guyi.component.message.stream.api.entry.Message;
import tech.guyi.component.message.stream.api.entry.MessageConsumerEntry;
import tech.guyi.component.message.stream.email.extract.TitleExtractor;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于邮件实现的消息流 <br />
 * 收到的邮件表示消息流的输入 <br />
 * 发布消息表示发送邮件 <br />
 * @author guyi
 * @date 2021/1/16 15:30
 */
public class EmailMessageStream implements MessageStream, InitializingBean {

    private final AntPathMatcher matcher = new AntPathMatcher();
    private final List<MessageConsumerEntry> consumers = new LinkedList<>();


    @Resource
    private EmailService service;
    @Resource
    private TitleExtractor extractor;

    @Override
    public @NonNull String getName() {
        return "email";
    }

    @Override
    public void publish(Message message) {
        String title = this.extractor.getTitle(message.getTopic());
        this.service.send(message.getAddress(), title, new String(message.getContent()));
    }

    @Override
    public void register(MessageConsumerEntry consumer) {
        this.consumers.add(consumer);
    }

    @Override
    public void unregister(String topic) {
        this.consumers.stream()
                .filter(consumer -> consumer.getTopic().contains(topic))
                .collect(Collectors.toList())
                .forEach(consumer -> {
                    if (consumer.getTopic().size() == 1){
                        this.consumers.remove(consumer);
                    }else{
                        consumer.getTopic().remove(topic);
                    }
                });
    }

    @Override
    public void close() {
        this.consumers.clear();
        this.service.close();
    }

    @Override
    public void afterPropertiesSet() {
        this.service.onEmail(email -> {
            String topic = this.extractor.getTopic(email.getTitle());
            Message message = new Message();
            message.setStream(this.getName());
            message.setTopic(topic);
            message.setContent(email.getContent().getBytes(StandardCharsets.UTF_8));
            message.setAddress(email.getSource());
            this.consumers.stream()
                    .filter(consumer -> consumer.getTopic().stream().anyMatch(t -> matcher.match(t,topic)))
                    .forEach(consumer -> consumer.getReceiver().accept(message));
        });
    }
}

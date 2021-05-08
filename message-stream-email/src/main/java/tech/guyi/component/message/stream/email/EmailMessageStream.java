package tech.guyi.component.message.stream.email;

import lombok.NonNull;
import tech.guyi.component.message.stream.api.attach.AttachKey;
import tech.guyi.component.message.stream.api.stream.MessageReceiver;
import tech.guyi.component.message.stream.api.stream.MessageStream;
import tech.guyi.component.message.stream.email.extract.TitleExtractor;
import tech.guyi.component.message.stream.email.key.AddressAttachKey;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * <p>基于邮件实现的消息流.</p>
 * <p>收到的邮件表示消息流的输入.</p>
 * <p>发布消息表示发送邮件.</p>
 * @author guyi
 */
public class EmailMessageStream implements MessageStream<Boolean> {


    @Resource
    private EmailService service;
    @Resource
    private TitleExtractor extractor;

    @Override
    public @NonNull String getName() {
        return "email";
    }

    @Override
    public void close() {
        this.service.close();
    }

    @Override
    public void open(MessageReceiver receiver) {
        this.service.onEmail(email -> {
            String topic = this.extractor.getTopic(email.getTitle());
            receiver.accept(
                    topic,
                    email.getContent().getBytes(StandardCharsets.UTF_8),
                    Collections.singletonMap(AddressAttachKey.class,email.getSource()));
        });
    }

    @Override
    public Optional<Boolean> publish(String topic, byte[] bytes, Map<Class<? extends AttachKey>,Object> attach) {
        // 如果附加信息中不存在收件人地址, 则丢弃该消息
        boolean success = Optional.ofNullable(attach)
                .map(a -> a.get(AddressAttachKey.class))
                .map(Object::toString)
                .map(address -> {
                    String title = this.extractor.getTitle(topic);
                    this.service.send(address, title, new String(bytes));
                    return true;
                })
                .orElse(false);
        return Optional.of(success);
    }

}

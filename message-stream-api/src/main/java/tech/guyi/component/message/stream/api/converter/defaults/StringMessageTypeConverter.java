package tech.guyi.component.message.stream.api.converter.defaults;


import tech.guyi.component.message.stream.api.converter.MessageTypeConverter;

import java.nio.charset.StandardCharsets;

/**
 * 字符串消息类型转换器.
 * @author guyi
 */
public class StringMessageTypeConverter implements MessageTypeConverter<String> {

    @Override
    public Class<String> forType() {
        return String.class;
    }

    @Override
    public String from(byte[] bytes) {
        return new String(bytes);
    }

    @Override
    public <R extends String> R from(byte[] bytes, Class<R> type) {
        return type.cast(new String(bytes));
    }

    @Override
    public byte[] to(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
    }

}

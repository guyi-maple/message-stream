package tech.guyi.component.message.stream.api.converter.defaults;

import tech.guyi.component.message.stream.api.converter.MessageTypeConverter;

import java.nio.charset.StandardCharsets;

/**
 * @author guyi
 * @date 2021/1/19 00:17
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

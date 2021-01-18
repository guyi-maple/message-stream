package tech.guyi.component.message.stream.api.converter;

/**
 * 消息转换器
 * @author guyi
 * @date 2021/1/18 22:56
 */
public interface MessageTypeConverter<M> {

    Class<M> forType();

    /**
     * 从byte数组转换
     * @param bytes byte数组
     * @return 消息
     */
    M from(byte[] bytes);

    /**
     * 消息转换为byte数组
     * @param message 消息
     * @return byte数组
     */
    byte[] to(M message);

}

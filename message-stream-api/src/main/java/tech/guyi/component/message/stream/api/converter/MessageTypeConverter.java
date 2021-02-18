package tech.guyi.component.message.stream.api.converter;

/**
 * <p>消息转换器.</p>
 * <p>当消息内容需要自定义时可以实现此接口, 给定转换方式.</p>
 * @author guyi
 */
public interface MessageTypeConverter<M> {

    /**
     * 返回要转换的类型
     * @return 要转换的类型
     */
    Class<M> forType();

    /**
     * 从byte数组转换
     * @param bytes byte数组
     * @return 消息
     */
    M from(byte[] bytes);

    /**
     * 从byte数组转换
     * @param bytes byte数组
     * @param type 返回类型
     * @param <R> 返回类型
     * @return 消息
     */
    <R extends M> R from(byte[] bytes, Class<R> type);

    /**
     * 消息转换为byte数组
     * @param message 消息
     * @return byte数组
     */
    byte[] to(M message);

}

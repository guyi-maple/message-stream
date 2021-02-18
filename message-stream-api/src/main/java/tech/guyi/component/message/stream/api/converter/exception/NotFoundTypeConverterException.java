package tech.guyi.component.message.stream.api.converter.exception;

/**
 * 抛出此异常表示找不到自定义消息类型的转换器
 * @author guyi
 */
public class NotFoundTypeConverterException extends RuntimeException {

    public NotFoundTypeConverterException(Class<?> type) {
        super(String.format("找不到 [%s] 的类型转换器", type));
    }
}

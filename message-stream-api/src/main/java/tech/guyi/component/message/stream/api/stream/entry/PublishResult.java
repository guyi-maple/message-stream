package tech.guyi.component.message.stream.api.stream.entry;

import lombok.Getter;

/**
 * 消息推送的返回实体
 * @author guyi
 * @date 2021/2/18 10:46
 */
@Getter
public class PublishResult {

    private final Class<?> type;
    private final Object value;

    public PublishResult(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static PublishResult empty(){
        return new PublishResult(Void.class, null);
    }

    public static PublishResult success(boolean success){
        return new PublishResult(Boolean.class, success);
    }

}

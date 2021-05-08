package tech.guyi.component.message.stream.api.configuration;

import lombok.Data;

/**
 * @author guyi
 * @date 2021/5/8 15:56
 */
@Data
public class MessageStreamConfiguration {

    /**
     * 消息推送回调实体对象池对象数量
     */
    private int publishHookPoolSize = 2000;

}

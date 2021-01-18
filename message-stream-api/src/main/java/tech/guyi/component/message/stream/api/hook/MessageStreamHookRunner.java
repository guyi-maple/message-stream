package tech.guyi.component.message.stream.api.hook;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 消息流钩子执行器
 * @author guyi
 * @date 2021/1/18 18:09
 */
public class MessageStreamHookRunner implements InitializingBean {

    // 消息流钩子集合
    private final List<MessageStreamHook<Object>> hooks = new LinkedList<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.context.getBeansOfType(MessageStreamHook.class)
                .values()
                .forEach(hooks::add);
    }

    public <E,H extends MessageStreamHook<E>> void run(MessageStreamHookType<E,H> type, E entry){
        this.hooks.stream()
                .filter(hook -> type.getClasses().isAssignableFrom(hook.getClass()))
                .forEach(hook -> hook.on(entry));
    }

}

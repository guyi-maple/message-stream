package tech.guyi.component.message.stream.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 容器关闭事件回调
 * @author guyi
 * @date 2021/1/16 14:20
 */
public class OnApplicationContextClose implements ApplicationListener<ContextClosedEvent>, InitializingBean {

    private final List<MessageStream> streams = new LinkedList<>();

    @Resource
    private ApplicationContext context;
    @Override
    public void afterPropertiesSet() {
        this.streams.addAll(this.context.getBeansOfType(MessageStream.class).values());
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        // 关闭所有消息流
        this.streams.forEach(MessageStream::close);
    }

}

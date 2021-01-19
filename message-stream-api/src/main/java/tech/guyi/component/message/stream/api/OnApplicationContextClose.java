package tech.guyi.component.message.stream.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;

import javax.annotation.Resource;

/**
 * 容器关闭事件回调
 * @author guyi
 * @date 2021/1/16 14:20
 */
public class OnApplicationContextClose implements ApplicationListener<ContextClosedEvent>, InitializingBean {

    @Resource
    private MessageStreams streams;
    @Resource
    private MessageStreamWorker worker;

    // 关闭消息流操作
    private void close(){
        // 关闭所有消息流
        this.streams.close();
        // 关闭工作空间
        this.worker.shutdownNow();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.close();
    }

    @Override
    public void afterPropertiesSet() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }
}

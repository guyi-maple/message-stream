package tech.guyi.component.message.stream.api;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import tech.guyi.component.message.stream.api.stream.MessageStreams;
import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;

import javax.annotation.Resource;

/**
 * 容器关闭事件回调
 * @author guyi
 */
public class OnApplicationContextClose implements ApplicationListener<ContextClosedEvent> {

    @Resource
    private MessageStreams streams;
    @Resource
    private MessageStreamWorker worker;

    // 关闭消息流操作
    private void close(){
        // 关闭所有消息流
        this.streams.close();
        // 关闭工作空间
        this.worker.shutdown();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 当Spring容器关闭时关闭消息流
        this.close();
    }

}

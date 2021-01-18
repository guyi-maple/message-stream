package tech.guyi.component.message.stream.api.worker.defaults;

import tech.guyi.component.message.stream.api.worker.MessageStreamWorker;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author guyi
 * @date 2021/1/19 00:21
 */
public class DefaultMessageStreamWorker extends ScheduledThreadPoolExecutor implements MessageStreamWorker {

    public DefaultMessageStreamWorker(int corePoolSize) {
        super(corePoolSize);
    }

}

package tech.guyi.component.message.stream.rocketmq.creatoe;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqConfiguration;

import javax.annotation.Resource;

/**
 * 默认实现
 * @author guyi
 */
public class DefaultRocketmqCreator implements RocketmqCreator {

    @Resource
    private RocketmqConfiguration configuration;

    @Override
    public DefaultMQPushConsumer createConsumer() {
        return new DefaultMQPushConsumer(configuration.getGroupId());
    }

    @Override
    public DefaultMQProducer createProducer() {
        return new DefaultMQProducer(configuration.getGroupId());
    }

}

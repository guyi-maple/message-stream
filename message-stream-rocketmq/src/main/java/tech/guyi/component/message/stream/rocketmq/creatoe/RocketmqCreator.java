package tech.guyi.component.message.stream.rocketmq.creatoe;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * Rocketmq的消费者与生产者创建
 * @author guyi
 */
public interface RocketmqCreator {

    /**
     * 创建Rocketmq消费者
     * @return Rocketmq消费者
     */
    DefaultMQPushConsumer createConsumer(String groupId);

    /**
     * 创建Rocketmq生产者
     * @return Rocketmq生产者
     */
    DefaultMQProducer createProducer(String groupId);
}

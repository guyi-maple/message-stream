package tech.guyi.component.message.stream.rocketmq.creatoe;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqAliyunConfiguration;
import tech.guyi.component.message.stream.rocketmq.configuration.RocketmqConfiguration;

import javax.annotation.Resource;

/**
 * 适用于阿里云Rocketmq服务的实现
 * @author guyi
 */
public class AliyunRocketmqCreator implements RocketmqCreator {

    @Resource
    private RocketmqConfiguration configuration;
    @Resource
    private RocketmqAliyunConfiguration aliyunConfiguration;

    @Override
    public DefaultMQPushConsumer createConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
                configuration.getGroupId(),
                // 添加阿里云的认证信息
                new AclClientRPCHook(new SessionCredentials(aliyunConfiguration.getAccessKey(), aliyunConfiguration.getSecretKey())),
                new AllocateMessageQueueAveragely());

        // 如果开启了消息轨迹
        if (aliyunConfiguration.isCloud()){
            consumer.setAccessChannel(AccessChannel.CLOUD);
        }
        return consumer;
    }

    @Override
    public DefaultMQProducer createProducer() {
        return new DefaultMQProducer(
                configuration.getGroupId(),
                // 添加阿里云的认证信息
                new AclClientRPCHook(new SessionCredentials(aliyunConfiguration.getAccessKey(), aliyunConfiguration.getSecretKey()))
        );
    }

}

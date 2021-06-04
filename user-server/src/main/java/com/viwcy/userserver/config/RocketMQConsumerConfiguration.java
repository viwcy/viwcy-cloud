package com.viwcy.userserver.config;

import com.viwcy.userserver.service.RocketMQConsumeListenerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO //、、RocketMQ消费者配置
 *
 * <p> Title: RocketMQConsumer </p >
 * <p> Description: RocketMQConsumer </p >
 * <p> History: 2020/10/29 9:36 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Configuration
@Slf4j
public class RocketMQConsumerConfiguration {

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private Integer consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private Integer consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private Integer consumeMessageBatchMaxSize;

    @Autowired
    private RocketMQConsumeListenerProcessor rocketMQConsumeListenerProcessor;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.groupName);
        consumer.setNamesrvAddr(this.namesrvAddr);
        consumer.setConsumeThreadMin(this.consumeThreadMin);
        consumer.setConsumeThreadMax(this.consumeThreadMax);
        consumer.registerMessageListener(this.rocketMQConsumeListenerProcessor);
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        //如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //设置消费模型，集群还是广播，默认为集群。广播能保证至少一次消费，但是消费失败后不做重试
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //设置一次消费消息的条数，默认为1条
        consumer.setConsumeMessageBatchMaxSize(this.consumeMessageBatchMaxSize);
        try {
            //设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0], topicTag[1]);
            }
            consumer.start();
            log.info("RocketMQ-Consumer initialize successful，groupName = [{}]，namesrvAddr = [{}]", this.groupName, this.namesrvAddr);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.error("RocketMQ-consumer initialize failed，reason = [{}]", e.getCause());
        }
        return consumer;
    }

}

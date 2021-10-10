package com.lin.missyou.manager.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ComsumerSchedule implements CommandLineRunner {

    @Value("${rocketmq.consumer.consumer-group}")
    private String consumerGroup;

    @Value("${rocketmq.namesrv-addr}")
    private String namesrvAddr;

    public void messageListener() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.consumerGroup);
        consumer.setNamesrvAddr(this.namesrvAddr);

        consumer.subscribe("TopicTest","*");
        consumer.setConsumeMessageBatchMaxSize(1);

        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context)->{
            for (Message message:messages){
                System.out.println("消息："+ new String(message.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
    }
}

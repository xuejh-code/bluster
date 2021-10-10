package com.lin.missyou.manager.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class ProducerSchedule {

    private DefaultMQProducer producer;

    @Value("${rocketmq.producer.producer-group}")
    private String producerGroup;

    @Value("${rocketmq.namesrv-addr}")
    private String namesrvAddr;

    public ProducerSchedule() {

    }

//    @PostConstruct
    public void defaultMQProducer(){
        if (this.producer == null){
            this.producer = new DefaultMQProducer(this.producerGroup);
            this.producer.setNamesrvAddr(this.namesrvAddr);
            this.producer.setSendMessageWithVIPChannel(false);
        }
        try {
            this.producer.start();
            System.out.println("---------producer start");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String send(String topic,String messageText) throws Exception{
        Message message = new Message(topic,messageText.getBytes());
        message.setDelayTimeLevel(4);

        SendResult result = this.producer.send(message);
        System.out.println(result.getMsgId());
        System.out.println(result.getSendStatus());

        return result.getMsgId();
    }
}

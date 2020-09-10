package com.alibaba.learning.service;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.net.www.MessageHeader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/9/6 15:42
 **/
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RocketMessageService implements IMessageService {
    private final RocketMQTemplate rocketMQTemplate;
    @Override
    public void sendMessage(String topic,Object message,Map<String,Object> headers) {
        Assert.notNull(message);
        headers.put("username","vincent");
        headers.put("content","nothing ...");
        rocketMQTemplate.convertAndSend(topic,message,headers);
    }

    @Override
    public void sendTransactionMessage(String topic,
                                       Object message,
                                       Map<String, Object> header) {
        rocketMQTemplate.sendMessageInTransaction(
                        "tx-test-group",topic,
                        MessageBuilder.createMessage(message, new MessageHeaders(header))
                ,
                        message
        ) ;
    }
}

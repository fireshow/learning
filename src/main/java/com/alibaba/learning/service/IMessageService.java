package com.alibaba.learning.service;


import org.springframework.messaging.Message;

import java.util.Map;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/9/6 11:46
 **/
public interface IMessageService {
     void sendMessage(String topic,Object message,Map<String,Object> headers);
     void sendTransactionMessage(String topic, Object message, Map<String,Object> header);
}

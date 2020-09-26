package com.alibaba.learning.repository;

import com.alibaba.learning.configs.RocketMqConfig;
import com.alibaba.learning.configs.mq.CustomBinding;
import com.alibaba.learning.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/9/26 12:28
 **/
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookMessageRepository {
    private final CustomBinding customBinding;
    @StreamListener(value=CustomBinding.INPUT_CHANNEL)
    public void handleMessage(@Payload Book messageBody, @Headers Map<String,Object> headers) throws InterruptedException {
        log.warn("收到的信息是：{},headers 是 ：{}",messageBody,headers);
        TimeUnit.MILLISECONDS.sleep(100);
        log.debug("message processed!");
    }
    public Boolean sendMessage(@NotNull Book book){
      return customBinding.output().send(
              MessageBuilder
                      .withPayload(book)
                      .setHeaderIfAbsent(RocketMQHeaders.TAGS,"Normal")
                      .setHeaderIfAbsent(RocketMQHeaders.KEYS,book.getId())
                      .build(),
              10
      ) ;

    }
}

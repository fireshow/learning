package com.alibaba.learning.repository;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.learning.configs.RocketMqConfig;
import com.alibaba.learning.configs.mq.CustomBinding;
import com.alibaba.learning.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ErrorMessage;
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
        if (RandomUtil.randomBoolean())
        {
            log.warn("收到的信息是：{},headers 是 ：{}",messageBody,headers);
            TimeUnit.MILLISECONDS.sleep(100);
            log.debug("message processed!");
        }
        else{
            throw new RuntimeException("message handler error");
        }

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
    @StreamListener(value = MessageHeaders.ERROR_CHANNEL)
    public void errorMessageHandler(Message<?> message){
        ErrorMessage errorMessage =(ErrorMessage)message;
        log.error("  @StreamListener(value = MessageHeaders.ERROR_CHANNEL)  stream error :{}",errorMessage);
    }
    @ServiceActivator(inputChannel = "book.book-consumer-group.errors")
    public void handleError(ErrorMessage message) {
        Throwable throwable = message.getPayload();
        log.error("截获异常", throwable);
        Message<?> originalMessage = message.getOriginalMessage();
        assert originalMessage != null;
        log.warn("@ServiceActivator(inputChannel = \"book.book-consumer-group.errors\")");
        log.info("原始消息体 = {}", new String((byte[]) originalMessage.getPayload()));
    }
}

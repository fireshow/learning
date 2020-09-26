package com.alibaba.learning.configs.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/9/26 12:14
 **/
public interface CustomBinding{
    String INPUT_CHANNEL ="book-in";
    String OUTPUT_CHANNEL ="book-out";
    @Input(INPUT_CHANNEL)
    SubscribableChannel input();
    @Output(OUTPUT_CHANNEL)
    MessageChannel output();
}

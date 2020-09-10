package com.alibaba.learning.service;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/9/6 17:05
 **/
@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup="tx-test-group")
public class LocalTransaction implements RocketMQLocalTransactionListener{
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.warn("executeLocalTransaction===>message:{},arg:{}",msg,arg);
      try {
          if (RandomUtil.randomBoolean())
          {
              throw new Exception("error has happened");
          }
          else {
              return RocketMQLocalTransactionState.COMMIT;
          }
      }catch (Exception e)
      {
          log.error("error: rocketMQ message send failed");
          return RocketMQLocalTransactionState.ROLLBACK;
      }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.warn("checkLocalTransaction====>message:{}",msg);
        return RocketMQLocalTransactionState.COMMIT;
    }

}

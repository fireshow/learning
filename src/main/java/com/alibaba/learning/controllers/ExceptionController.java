package com.alibaba.learning.controllers;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/25 15:35
 **/
@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(value = BlockException.class)
    public String sentinelExcetionHandle(BlockException e){
        log.error("Error : \n",e);
        return "触发了Sentinel降级/限流规则！";
    }
    @ExceptionHandler(value = IOException.class)
    public String ioExceptionHandle(IOException e){
        log.error("Error : \n",e);
        return "IO错误！";
    }
    @ExceptionHandler(value = Exception.class)
    public String finalExceptionHandle(Exception e){
        log.error("Error : \n",e);
        return e.getLocalizedMessage();
    }
}

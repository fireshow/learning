package com.alibaba.learning.controllers.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/9 21:06
 **/
@Controller
public class IndexController {
    @GetMapping("/")
    public String indexPage(){
        return "index";
    }
}

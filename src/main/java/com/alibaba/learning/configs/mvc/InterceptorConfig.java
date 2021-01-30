package com.alibaba.learning.configs.mvc;

import com.alibaba.learning.configs.mvc.interceptor.BookInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Vincent(sec1995 @ hotmail.com)
 * @version 1.0
 * @date 2020/11/6 19:54
 **/
@Configuration
public class InterceptorConfig  implements WebMvcConfigurer {
    @Bean
    public BookInterceptor bookInterceptor(){
        return new BookInterceptor();
    }
    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bookInterceptor())
                .addPathPatterns("/book/*")
                .order(0);
    }
}

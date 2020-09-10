package com.alibaba.learning.configs;

import com.github.javafaker.Faker;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Locale;


/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/7 17:08
 **/
@Configuration
public class EsConfiguration  {
    @Bean
    @Primary
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("180.76.184.26", 9200, "http")));
    }
    @Bean
    public Faker faker(){
      return new Faker(Locale.CHINESE);
    }
}

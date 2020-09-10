package com.alibaba.learning.controllers.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.learning.repository.BookRepository;
import com.alibaba.learning.entity.Book;
import com.alibaba.learning.entity.GoodsInfo;
import com.alibaba.learning.service.IMessageService;
import com.alibaba.learning.utils.EsUtil;
import com.alibaba.learning.utils.HtmlParseUtil;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date  2020/4/14 20:43
 **/
@RestController
@Slf4j
public class BaseController {
    @Resource
    private  HtmlParseUtil parseUtil;
    @Resource
    private  BookRepository bookRepository;
    @Resource
    private  EsUtil esUtil;
    @Resource
    private  Faker faker;
    @Resource
    private IMessageService rocketMessageService;

    @SentinelResource("hello")
    @GetMapping(value = "/hello/{name}")
    public ResponseEntity<String> hello(@PathVariable String name)
    {
        log.info("我被访问了.....");
        return new ResponseEntity<>("hello" + name, HttpStatus.OK);
    }
    @GetMapping("/jd/{keyWord}")
    public List<GoodsInfo> batchInsertDataToES(@PathVariable String keyWord)
    {
      try {
          List<GoodsInfo> goodsInfos = parseUtil.webParseOfJD(keyWord);
          if (esUtil.batchInsert(goodsInfos))
          {
              log.info("ES 数据批量插入成功！！！");
          }
          return goodsInfos;
      }catch (IOException ioException)
      {
          log.error("出错了。。。:\n ",ioException);
          return null;
      }
    }
    @GetMapping("/jd/query")
    public List<Map<String,Object>> queryData(@RequestParam String index,
                                              @RequestParam String filedName,
                                              @RequestParam String value) throws IOException {
        return esUtil.queryData(index,filedName,value);
    }
    @GetMapping("/book/all")
    @ResponseBody
    public Iterable<Book> findAllBooks(){
        return bookRepository.findAll();
    }
    @GetMapping("/book/{value}")
    @ResponseBody
    public Iterable<Book> findByAuthor(@PathVariable("value") String value){
        return bookRepository.findBookByAuthor(value);
    }
    @GetMapping("/book")
    @ResponseBody
    public Iterable<Book> findAll(){
        return bookRepository.findAll();
    }
    @PutMapping("/book")
    public ResponseEntity<Book> save(@RequestBody(required = false)  Book book){
        if (null==book)
        {
            book= Book.builder()
                    .author(faker.book().author())
                    .bookName(faker.book().title())
                    .content(faker.leagueOfLegends().masteries())
                    .id(faker.idNumber().valid())
                    .price(faker.random().nextDouble())
                    .printDate(faker.date().past(365*50, TimeUnit.DAYS))
                    .build();
        }
        log.debug("send message to [topic-book]");
        Map<String,Object>headers =new HashMap<>();
        headers.put(RocketMQHeaders.KEYS,book.getId());
        headers.put(RocketMQHeaders.TAGS,"TagB");
        rocketMessageService.sendMessage("topic-book",book,headers);
        log.debug("send  traction message to [tx-book-topic]");
        rocketMessageService.sendTransactionMessage("tx-book-topic",book,headers);
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
    }
}

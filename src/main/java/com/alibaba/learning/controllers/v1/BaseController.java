package com.alibaba.learning.controllers.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.learning.repository.BookMessageRepository;
import com.alibaba.learning.repository.BookRepository;
import com.alibaba.learning.entity.Book;
import com.alibaba.learning.entity.GoodsInfo;
import com.alibaba.learning.service.IMessageService;
import com.alibaba.learning.utils.EsUtil;
import com.alibaba.learning.utils.HtmlParseUtil;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseController {
    private final HtmlParseUtil parseUtil;
    private final BookRepository bookRepository;
    private final EsUtil esUtil;
    private final IMessageService rocketMessageService;
    private final BookMessageRepository bookMessageRepository;


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
            book=Book.getRandomEntity();
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
    @GetMapping("/book-stream")
    public ResponseEntity<Book> addBookJf(@RequestBody(required = false)  Book book)
    {
        if (null==book)
        {
            book=Book.getRandomEntity();
        }
        Boolean send_res = bookMessageRepository.sendMessage(book);
        if (send_res)
        {
            log.info("Send Message Success!");
            return ResponseEntity.ok(book);
        }else {
            return  ResponseEntity.badRequest().build();
        }
    }
}

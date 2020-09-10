package com.alibaba.learning.repository;

import com.alibaba.learning.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/16 21:11
 **/
@Service
public interface BookRepository extends ElasticsearchRepository<Book,String> {
    /**
     *
     * @param author
     * @return
     */
    List<Book> findBookByAuthor(String author);
    List<Book> findBookByPrintDateBetween(Date startDate,Date endDate);
}

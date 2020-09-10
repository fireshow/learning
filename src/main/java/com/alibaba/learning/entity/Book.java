package com.alibaba.learning.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/16 20:47
 **/
@Data
@Builder
@Document(indexName ="dangdang",type = "book")
public class Book implements Serializable {
    @Id
    @NotNull
    private String id;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    @NotBlank
    private String bookName;
    @Field(type = FieldType.Keyword)
    @NotNull
    private String author;
    @Field(type = FieldType.Double)
    @Range(min = 0,message = "价格必须大于等于0")
    private Double price;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Date,format = DateFormat.basic_date_time_no_millis)
    @Past(message = "印刷日期必须在今天之前")
    private Date printDate;
    private long millSeconds;
}

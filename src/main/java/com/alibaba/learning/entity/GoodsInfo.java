package com.alibaba.learning.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/13 21:56
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(indexName = "goods_info")
public class GoodsInfo implements Serializable {
    @Id
    private String uid;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String goodsName;
    private String imgSrc;
    @Field(type = FieldType.Text)
    private String price;
}

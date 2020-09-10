package com.alibaba.learning.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.search.Search;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/10 21:00
 **/
@Component
public class EsUtil implements Closeable {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public void close() throws IOException {
        if (!Objects.isNull(client))
        {
            client.close();
        }
    }

    /**
     * 批量插入数据
     * @param datas 数据
     * @param <T> 泛型
     * @return 插入结果
     * @throws IOException IO异常
     */
    public <T> Boolean batchInsert(List<T> datas) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("60s");
        if (CollectionUtil.isNotEmpty(datas))
        {
            for (T data : datas) {
                bulkRequest.add(
                        new IndexRequest("jd_goods")
                        .source(JSONUtil.toJsonStr(data), XContentType.JSON)
                );
            }
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * 根据索引，类型以及参数名和值查询
     * @param index 索引名称
     * @return 查询结果
     * @throws IOException
     */
    public List<Map<String,Object>> queryData(String index,String filed,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        List<Map<String,Object>> res=new LinkedList<>();
        searchRequest.indices(index);
        SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource();
           sourceBuilder.query(QueryBuilders.matchQuery(filed,value));
        searchRequest.source(sourceBuilder);
        //获取结果
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        Assert.notNull(hits,"查询结果为空");
        if (RestStatus.OK.equals(response.status()) && response.getHits().getTotalHits().value > 0) {
            for (SearchHit hit : hits.getHits()) {
                res.add(hit.getSourceAsMap());
            }
        }
        return res;
    }

}

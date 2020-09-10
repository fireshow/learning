package com.alibaba.learning.utils;

import com.alibaba.learning.entity.GoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Vincent(wenzheng.shao @ hand - china.com)
 * @version 1.0
 * @date 2020/6/13 21:44
 **/
@Component
@Primary
@Slf4j
public class HtmlParseUtil {
    /**
     * 请求地址
     */
    private static final String URI_JD ="https://search.jd.com/Search?keyword=";
    /**
     * 获取京东商品页面的Json数据
     * @param key 查询关键字
     * @return 商品信息
     * @throws IOException IO异常
     */
    public List<GoodsInfo> webParseOfJD(String key) throws IOException {
        List<GoodsInfo> goodsInfos=new LinkedList<>();
        //解析网页
        Document document = Jsoup.parse(new URL(URI_JD +key), 30000);
        Element element = document.getElementById("J_goodsList");
        if (Objects.nonNull(element))
        {
            //获取所有的li标签
            Elements lis = element.getElementsByTag("li");
            for (Element li : lis) {
                GoodsInfo goodsInfo = GoodsInfo.builder()
                        //名称
                        .goodsName(li.getElementsByClass("p-name").get(0).text())
                        //价格
                        .price(li.getElementsByClass("p-price").get(0).text())
                        //商品照片
                        .imgSrc(li.getElementsByTag("img").get(0).attr("src"))
                        .build();
                goodsInfos.add(goodsInfo);
            }
        }
        return goodsInfos;
    }
}

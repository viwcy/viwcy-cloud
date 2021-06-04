package com.viwcy.searchserver.viwcyuser.service.impl;

import com.viwcy.searchserver.viwcyuser.entity.UserArticleItem;
import com.viwcy.searchserver.viwcyuser.service.UserArticleService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO //
 *
 * <p> Title: UserArticleServiceImpl </p >
 * <p> Description: UserArticleServiceImpl </p >
 * <p> History: 2021/5/19 17:32 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Slf4j
@Service
public class UserArticleServiceImpl implements UserArticleService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 分词高亮查询
     */
    @Override
    public List<UserArticleItem> queryHightList(UserArticleItem item) {
        //根据一个值查询多个字段  并高亮显示  这里的查询是取并集，即多个字段只需要有一个字段满足即可
        //需要查询的字段
        //should表示or，must表示and
        //QueryBuilders.matchQuery表示单个匹配
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", item.getValue()))
                .should(QueryBuilders.matchQuery("content", item.getValue()));
        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("title"), new HighlightBuilder.Field("content"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();
        //查询
        SearchHits<UserArticleItem> search = elasticsearchRestTemplate.search(searchQuery, UserArticleItem.class);
        //得到查询返回的内容
        List<SearchHit<UserArticleItem>> searchHits = search.getSearchHits();
        //设置一个最后需要返回的实体类集合
        List<UserArticleItem> list = new ArrayList<>();
        //遍历返回的内容进行处理
        for (SearchHit<UserArticleItem> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTitle(highlightFields.get("title") == null ? searchHit.getContent().getTitle() : highlightFields.get("title").get(0));
            searchHit.getContent().setContent(highlightFields.get("content") == null ? searchHit.getContent().getContent() : highlightFields.get("content").get(0));
            //放到实体类中
            list.add(searchHit.getContent());
        }
        return list;
    }

    /**
     * 分词高亮分页查询
     */
    @Override
    public Page<UserArticleItem> queryHightPage(UserArticleItem item) {
        //根据一个值查询多个字段  并高亮显示  这里的查询是取并集，即多个字段只需要有一个字段满足即可
        //需要查询的字段
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", item.getValue()))
                .should(QueryBuilders.matchQuery("content", item.getValue()));
        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //基本查询条件
                .withQuery(boolQueryBuilder)
                //高亮
                .withHighlightFields(new HighlightBuilder.Field("title"), new HighlightBuilder.Field("content"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                //分页
                .withPageable(PageRequest.of(item.getPage() - 1, item.getSize()))
                //排序
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .build();
        //查询
        SearchHits<UserArticleItem> search = elasticsearchRestTemplate.search(searchQuery, UserArticleItem.class);
        //得到查询返回的内容
        List<SearchHit<UserArticleItem>> searchHits = search.getSearchHits();
        //设置一个最后需要返回的实体类集合
        List<UserArticleItem> list = new ArrayList<>();
        //遍历返回的内容进行处理
        for (SearchHit<UserArticleItem> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTitle(highlightFields.get("title") == null ? searchHit.getContent().getTitle() : highlightFields.get("title").get(0));
            searchHit.getContent().setContent(highlightFields.get("content") == null ? searchHit.getContent().getContent() : highlightFields.get("content").get(0));
            //放到实体类中
            list.add(searchHit.getContent());
        }
        return new PageImpl<>(list, PageRequest.of(item.getPage() - 1, item.getSize()), search.getTotalHits());
    }
}

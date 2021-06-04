package com.viwcy.searchserver.viwcyuser.entity;

import com.viwcy.searchserver.entity.ESBaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * TODO //、、ES切记不能和MP或mybatis共用实体，否则StackOverflow
 *
 * <p> Title: UserArticle </p >
 * <p> Description: UserArticle </p >
 * <p> History: 2021/5/19 17:00 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
@Document(indexName = "user_article", shards = 5)
public class UserArticleItem extends ESBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private Long userId;
    /**
     * 【keyword】：数据类型用来建立电子邮箱地址、姓名、邮政编码和标签等数据类型，不需要进行分词。可以被用来检索过滤、排序和聚合。keyword 类型字段只能用本身来进行检索。
     * <p>
     * 【text】：Text 数据类型被用来索引长文本，这些文本会被分析，在建立索引前会将这些文本进行分词，转化为词的组合，建立索引。
     * 比如你配置了IK分词器，那么就会进行分词，搜索的时候会搜索分词来匹配这个text文档。但是：text 数据类型不能用来排序和聚合
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    private Long createId;
    private String createName;
    private Long updateId;
    private String updateName;

    private String value;
}

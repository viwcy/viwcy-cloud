package com.viwcy.searchserver.viwcyuser.service;

import com.viwcy.searchserver.viwcyuser.entity.UserArticleItem;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * TODO //
 *
 * <p> Title: UserArticleService </p >
 * <p> Description: UserArticleService </p >
 * <p> History: 2021/5/19 17:31 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
public interface UserArticleService {

    /**
     * 分词高亮查询
     */
    List<UserArticleItem> queryHightList(UserArticleItem item);

    /**
     * 分词高亮分页查询
     */
    Page<UserArticleItem> queryHightPage(UserArticleItem item);
}

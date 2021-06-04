package com.viwcy.userserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viwcy.modelrepository.viwcyuser.entity.UserArticle;

/**
 * TODO //
 *
 * <p> Title: UserArticleService </p >
 * <p> Description: UserArticleService </p >
 * <p> History: 2021/5/19 17:04 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
public interface UserArticleService extends IService<UserArticle> {

    /**
     * 用户新增文章
     */
    boolean save(UserArticle userArticle);
}

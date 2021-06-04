package com.viwcy.searchserver.viwcyuser.api;

import com.viwcy.basecommon.common.BaseController;
import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.searchserver.viwcyuser.entity.UserArticleItem;
import com.viwcy.searchserver.viwcyuser.service.UserArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO //
 *
 * <p> Title: UserArticleApi </p >
 * <p> Description: UserArticleApi </p >
 * <p> History: 2021/5/19 17:35 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@RestController
@RequestMapping("/article")
public class UserArticleApi extends BaseController {

    @Autowired
    private UserArticleService userArticleService;

    /**
     * 分词高亮查询
     */
    @PostMapping("/hightList")
    public ResultEntity hightList(@RequestBody UserArticleItem item) {
        return success(userArticleService.queryHightList(item));
    }

    /**
     * 分词高亮分页查询
     */
    @PostMapping("/hightPage")
    public ResultEntity hightPage(@RequestBody UserArticleItem item) {
        return success(userArticleService.queryHightPage(item));
    }
}

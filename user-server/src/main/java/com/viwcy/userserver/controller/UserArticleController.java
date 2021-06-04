package com.viwcy.userserver.controller;

import com.viwcy.basecommon.aspect.LoginUser;
import com.viwcy.basecommon.common.BaseController;
import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.basecommon.constant.LoginUserEnum;
import com.viwcy.modelrepository.viwcyuser.entity.UserArticle;
import com.viwcy.userserver.service.UserArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO //
 *
 * <p> Title: UserArticleController </p >
 * <p> Description: UserArticleController </p >
 * <p> History: 2021/5/19 17:10 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@RestController
@RequestMapping("/article")
public class UserArticleController extends BaseController {

    @Autowired
    private UserArticleService userArticleService;

    /**
     * 用户新增文章
     */
    @PostMapping("/save")
    @LoginUser(type = LoginUserEnum.CREATE_AND_UPDATE)
    public ResultEntity save(@RequestBody UserArticle userArticle) {
        if (StringUtils.isBlank(userArticle.getTitle())) {
            return hint("文章标题不能为空");
        }
        if (StringUtils.isBlank(userArticle.getContent())) {
            return hint("文章内容不能为空");
        }
        boolean save = userArticleService.save(userArticle);
        return save ? success("文章添加成功") : fail("文章添加失败");
    }
}

package com.viwcy.userserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viwcy.basecommon.constant.TopicConstant;
import com.viwcy.basecommon.exception.BusinessException;
import com.viwcy.basecommon.util.JwtUtil;
import com.viwcy.modelrepository.viwcyuser.entity.UserArticle;
import com.viwcy.modelrepository.viwcyuser.mapper.UserArticleMapper;
import com.viwcy.userserver.service.RocketMQProducer;
import com.viwcy.userserver.service.UserArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO //
 *
 * <p> Title: UserArticleServiceImpl </p >
 * <p> Description: UserArticleServiceImpl </p >
 * <p> History: 2021/5/19 17:04 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Service
@Slf4j
public class UserArticleServiceImpl extends ServiceImpl<UserArticleMapper, UserArticle> implements UserArticleService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RocketMQProducer rocketMQProducer;

    /**
     * 用户新增文章
     */
    @Override
    public boolean save(UserArticle userArticle) {
        userArticle.setUserId(Long.parseLong(jwtUtil.getUserId()));
        int insert = this.baseMapper.insert(userArticle);
        if (insert != 1) {
            throw new BusinessException("文章保存失败");
        }
        try {
            //TODO  暂时先使用mq保证数据一致性，后期可以利用binlog日志组件canal
            rocketMQProducer.syncSend(TopicConstant.USER_SERVER_TOPIC, TopicConstant.USER_SERVER_ARTICLE_TAG, JSON.toJSONString(userArticle));
            log.info("MQ消息发送成功，主键ID = [{}]", userArticle.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MQ消息发送失败，主键ID = [{}]，原因 = [{}]", userArticle.getId(), e.getMessage());
        }
        return true;
    }
}

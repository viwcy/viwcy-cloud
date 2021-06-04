package com.viwcy.userserver.service;

import com.viwcy.basecommon.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO //
 *
 * <p> Title: PayTopicBeanFactory </p >
 * <p> Description: PayTopicBeanFactory </p >
 * <p> History: 2020/11/4 14:45 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Component
public class TopicBeanFactory extends AbstractBeanFactory {

    @Autowired
    private Map<String, TopicService> topics = new ConcurrentHashMap<>(2);

    /**
     * 根据type类型获取不同的实例对象
     */
    @Override
    protected TopicService topic(String type) {
        TopicService topicService = topics.get(type);
        if (topicService == null) {
            throw new BusinessException("类型暂不支持");
        }
        return topicService;
    }
}

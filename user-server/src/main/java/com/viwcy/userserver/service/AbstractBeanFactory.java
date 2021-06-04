package com.viwcy.userserver.service;

/**
 * TODO //、、抽象工厂
 *
 * <p> Title: BeanFactory </p >
 * <p> Description: BeanFactory </p >
 * <p> History: 2021/4/21 10:56 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
public abstract class AbstractBeanFactory {

    protected abstract TopicService topic(String type);
}

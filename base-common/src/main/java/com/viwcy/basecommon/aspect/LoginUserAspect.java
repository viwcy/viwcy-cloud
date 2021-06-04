package com.viwcy.basecommon.aspect;

import com.viwcy.basecommon.constant.LoginUserEnum;
import com.viwcy.basecommon.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * TODO //
 *
 * <p> Title: LoginUserAspect </p >
 * <p> Description: LoginUserAspect </p >
 * <p> History: 2021/4/13 15:17 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Aspect
@Component
@Slf4j
public class LoginUserAspect {

    @Autowired
    private JwtUtil jwtUtil;

    private final LocalDateTime date = LocalDateTime.now();

    @Pointcut("@annotation(com.viwcy.basecommon.aspect.LoginUser)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void around(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LoginUser loginUser = method.getAnnotation(LoginUser.class);
        Map<String, Object> userInfo = jwtUtil.getUserInfo();
        if (LoginUserEnum.CREATE.equals(loginUser.type())) {
            create(point, userInfo);
        } else if (LoginUserEnum.UPDATE.equals(loginUser.type())) {
            update(point, userInfo);
        } else {
            createAndUpdate(point, userInfo);
        }
    }

    private void create(JoinPoint point, Map<String, Object> userInfo) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            Object argument = args[0];
            BeanWrapper beanWrapper = new BeanWrapperImpl(argument);
            create(beanWrapper, userInfo);
            log.info("LoginUserAspect CREATE = {}", ToStringBuilder.reflectionToString(argument, ToStringStyle.SHORT_PREFIX_STYLE));
        }
    }

    private void update(JoinPoint point, Map<String, Object> userInfo) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            Object argument = args[0];
            BeanWrapper beanWrapper = new BeanWrapperImpl(argument);
            update(beanWrapper, userInfo);
            log.info("LoginUserAspect UPDATE = {}", ToStringBuilder.reflectionToString(argument, ToStringStyle.SHORT_PREFIX_STYLE));
        }
    }

    private void createAndUpdate(JoinPoint point, Map<String, Object> userInfo) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            Object argument = args[0];
            BeanWrapper beanWrapper = new BeanWrapperImpl(argument);
            create(beanWrapper, userInfo);
            update(beanWrapper, userInfo);
            log.info("LoginUserAspect CREATE AND UPDATE = {}", ToStringBuilder.reflectionToString(argument, ToStringStyle.SHORT_PREFIX_STYLE));
        }
    }

    private void create(BeanWrapper beanWrapper, Map<String, Object> userInfo) {
        if (beanWrapper.isWritableProperty("createUser")) {
            beanWrapper.setPropertyValue("createUser", userInfo.get("id"));
        }
        if (beanWrapper.isWritableProperty("createId")) {
            beanWrapper.setPropertyValue("createId", userInfo.get("id"));
        }
        if (beanWrapper.isWritableProperty("createName")) {
            beanWrapper.setPropertyValue("createName", userInfo.get("userName"));
        }
        if (beanWrapper.isWritableProperty("createTime")) {
            beanWrapper.setPropertyValue("createTime", date);
        }
    }

    private void update(BeanWrapper beanWrapper, Map<String, Object> userInfo) {
        if (beanWrapper.isWritableProperty("updateUser")) {
            beanWrapper.setPropertyValue("updateUser", userInfo.get("id"));
        }
        if (beanWrapper.isWritableProperty("updateId")) {
            beanWrapper.setPropertyValue("updateId", userInfo.get("id"));
        }
        if (beanWrapper.isWritableProperty("updateName")) {
            beanWrapper.setPropertyValue("updateName", userInfo.get("userName"));
        }
        if (beanWrapper.isWritableProperty("updateTime")) {
            beanWrapper.setPropertyValue("updateTime", date);
        }
    }
}

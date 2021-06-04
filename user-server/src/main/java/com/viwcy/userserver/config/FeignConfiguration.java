package com.viwcy.userserver.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO //
 *
 * <p> Title: FeignConfiguration </p >
 * <p> Description: FeignConfiguration </p >
 * <p> History: 2020/9/21 10:39 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Configuration
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.BASIC;
    }
}

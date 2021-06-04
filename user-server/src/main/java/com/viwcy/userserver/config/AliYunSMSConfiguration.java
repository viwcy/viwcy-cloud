package com.viwcy.userserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO //
 *
 * <p> Title: AliYunMessageConfig </p >
 * <p> Description: AliYunMessageConfig </p >
 * <p> History: 2020/12/2 9:26 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliYunSMSConfiguration {

    /**
     * keyId
     */
    private String accessKeyId;

    /**
     * keySecret
     */
    private String accessKeySecret;

    /**
     * signName
     */
    private String signName;

    /**
     * 注册用户信息，短信通知的模板，要配合通用签名使用
     */
    private String registerTemplateCode;

    /**
     * 短信登录，发送验证码短信，配合验证码短信签名使用
     */
    private String loginTemplateCode;
}

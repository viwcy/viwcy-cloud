package com.viwcy.userserver.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.viwcy.basecommon.constant.GlobalRedisPrefix;
import com.viwcy.basecommon.util.NumberUtil;
import com.viwcy.userserver.config.AliYunSMSConfiguration;
import com.viwcy.userserver.constant.RedisPrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * TODO //
 *
 * <p> Title: AliYunMessageUtil </p >
 * <p> Description: AliYunMessageUtil </p >
 * <p> History: 2020/12/1 17:09 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Slf4j
@Component
public class AliYunMessageUtil {

    @Autowired
    private AliYunSMSConfiguration aliYunSMSConfiguration;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NumberUtil numberUtil;

    /**
     * @param mobile
     * @return com.aliyuncs.dysmsapi.viwcyuser.v20170525.SendSmsResponse
     * @Description TODO    用户注册发送短信通知（暂不用，因没有通用模板签名）
     * @date 2020/12/2 11:00
     * @author Fuqiang
     */
    public SendSmsResponse registerSend(String mobile) throws ClientException {
        return send(mobile, aliYunSMSConfiguration.getRegisterTemplateCode(), null);
    }

    /**
     * @param mobile
     * @return com.aliyuncs.dysmsapi.viwcyuser.v20170525.SendSmsResponse
     * @Description TODO    短信登录发送验证码
     * @date 2020/12/2 10:59
     * @author Fuqiang
     */
    public SendSmsResponse loginSend(String mobile) throws ClientException {
        HashMap<String, String> map = new HashMap<>(1);
        String code = numberUtil.randomNumber(6);
        //key是自定义短信模板的取值符号
        map.put("code", code);
        SendSmsResponse response = send(mobile, aliYunSMSConfiguration.getLoginTemplateCode(), map);
        if (response.getCode() != null && response.getCode().equals("OK")) {
            //缓存短信验证码，5分钟
            redisTemplate.opsForValue().set(GlobalRedisPrefix.USER_SERVER + RedisPrefix.LOGIN_CODE + mobile, code, 5, TimeUnit.MINUTES);
            log.info("【验证码：{}，有效期5分钟】", code);
        }
        return response;
    }

    /**
     * @param mobile       手机号
     * @param templateCode 短信模板
     * @param map          自定义参数，如短信验证码，用户名称等
     * @return com.aliyuncs.dysmsapi.viwcyuser.v20170525.SendSmsResponse
     * @Description TODO    通用短信发送
     * @date 2020/12/2 10:59
     * @author Fuqiang
     */
    private SendSmsResponse send(String mobile, String templateCode, HashMap<String, String> map) throws ClientException {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(mobile);
        request.setSignName(aliYunSMSConfiguration.getSignName());
        request.setTemplateCode(templateCode);
        //自定义参数或短信验证码code等等
        if (!ObjectUtils.isEmpty(map)) {
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        }
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliYunSMSConfiguration.getAccessKeyId(), aliYunSMSConfiguration.getAccessKeySecret());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            log.info("【短信验证码发送成功，手机号为{}，请及时查收】", mobile);
        } else {
            log.error("【短信发送失败，原因：{}】", sendSmsResponse.getMessage());
        }
        return sendSmsResponse;
    }

}

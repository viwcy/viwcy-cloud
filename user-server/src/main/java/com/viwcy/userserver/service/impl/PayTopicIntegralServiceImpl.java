package com.viwcy.userserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viwcy.basecommon.exception.BusinessException;
import com.viwcy.modelrepository.viwcyuser.entity.OrderInfo;
import com.viwcy.modelrepository.viwcyuser.entity.UserWallet;
import com.viwcy.userserver.feign.handle.PayOrderHandle;
import com.viwcy.userserver.service.TopicService;
import com.viwcy.userserver.service.UserWalletService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * TODO //、、支付成功，增加用户积分
 *
 * <p> Title: PayTopicIntegralServiceImpl </p >
 * <p> Description: PayTopicIntegralServiceImpl </p >
 * <p> History: 2020/11/3 16:06 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Service("pay_topic@pay_topic_integral_tag")
@Slf4j
public class PayTopicIntegralServiceImpl implements TopicService {

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private PayOrderHandle payOrderHandle;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void execute(String content) {
        log.info("订单编号：{}", content);
        RLock lock = redissonClient.getLock("pay_topic_integral_tag_lock_");

        try {
            lock.lock();
            OrderInfo orderInfo = payOrderHandle.findOrderByNo(content).getData();
            log.info("获取支付订单，数据：{}", JSON.toJSONString(orderInfo));
            UserWallet userWallet = userWalletService.getOne(new QueryWrapper<UserWallet>().eq("user_id", orderInfo.getCreateUser()));
            //新增积分
            BigDecimal multiply = orderInfo.getOrderAmount().multiply(new BigDecimal(1000));
            userWallet.setNowIntegral(userWallet.getNowIntegral().add(multiply));
            boolean update = userWalletService.updateById(userWallet);
            if (update) {
                log.info("积分更新成功，用户ID：{}，新增积分：{}，当前积分：{}", orderInfo.getCreateUser(), multiply, userWallet.getNowIntegral());
            } else {
                log.error("积分更新失败");
                throw new BusinessException("积分更新失败");
            }
        } finally {
            lock.unlock();
        }
    }
}

package com.viwcy.userserver.feign.factory;

import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.modelrepository.viwcyuser.entity.OrderInfo;
import com.viwcy.userserver.feign.handle.PayOrderHandle;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO //
 *
 * <p> Title: UserHandleFactory </p >
 * <p> Description: UserHandleFactory </p >
 * <p> History: 2021/4/19 15:37 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Component
@Slf4j
public class PayOrderFallbackFactory implements FallbackFactory<PayOrderHandle> {


    @Override
    public PayOrderHandle create(Throwable throwable) {
        return new PayOrderHandle() {
            @Override
            public ResultEntity<OrderInfo> findOrderByNo(String orderNo) {
                log.error("Feign调用下游服务[user-server#queryPageUser(UserParam param)]发生熔断异常，Error Message = [{}]", throwable.toString());
                return ResultEntity.success("当前请求人数过多，请稍后重试");
            }
        };
    }
}

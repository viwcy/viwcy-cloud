package com.viwcy.userserver.feign.fallback;

import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.modelrepository.viwcyuser.entity.OrderInfo;
import com.viwcy.userserver.feign.handle.PayOrderHandle;
import org.springframework.stereotype.Component;

/**
 * TODO //
 *
 * <p> Title: PayOrderFallback </p >
 * <p> Description: PayOrderFallback </p >
 * <p> History: 2020/10/10 0:21 </p >
 * <pre>
 *      Copyright: Create by FQ, ltd. Copyright() 2020.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Component
public class PayOrderFallback implements PayOrderHandle {

    @Override
    public ResultEntity<OrderInfo> findOrderByNo(String orderNo) {
        return ResultEntity.success("当前请求人数过多，请稍后重试");
    }
}

package com.viwcy.userserver.feign.handle;

import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.modelrepository.viwcyuser.entity.OrderInfo;
import com.viwcy.userserver.config.FeignConfiguration;
import com.viwcy.userserver.feign.factory.PayOrderFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO //
 *
 * <p> Title: PayOrderHandle </p >
 * <p> Description: PayOrderHandle </p >
 * <p> History: 2020/10/10 0:20 </p >
 * <pre>
 *      Copyright: Create by FQ, ltd. Copyright() 2020.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@FeignClient(value = "pay-server", path = "/order", configuration = FeignConfiguration.class, fallbackFactory = PayOrderFallbackFactory.class)
public interface PayOrderHandle {

    @GetMapping("/findOrderByNo")
    ResultEntity<OrderInfo> findOrderByNo(@RequestParam("orderNo") String orderNo);
}

package com.viwcy.gatewayserver.handle;

import com.viwcy.gatewayserver.common.BaseController;
import com.viwcy.gatewayserver.common.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

/**
 * TODO //、、Hystrix服务熔断
 *
 * <p> Title: FallbackController </p >
 * <p> Description: FallbackController </p >
 * <p> History: 2021/4/15 9:30 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@RestController
@Slf4j
@RequestMapping("/fallback")
public class HystrixFallbackHandle extends BaseController {

    /**
     * default Hystrix fallback.
     */
    @PostMapping("/default")
    public ResultEntity defaultFallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("Hystrix服务熔断，Request URL = [{}]，Error Message = [{}]", delegate.getRequest().getURI(), exception.toString());
        return success("系统繁忙，请稍后重试");
    }

    /**
     * user-server Hystrix fallback.
     */
    @PostMapping("/user-server")
    public ResultEntity userServerFallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("【user-server】Hystrix服务熔断，Request URL = [{}]，Error Message = [{}]", delegate.getRequest().getURI(), exception.toString());
        return success("系统繁忙，请稍后重试");
    }

    /**
     * goods-server Hystrix fallback.
     */
    @PostMapping("/goods-server")
    public ResultEntity goodsServerFallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("【goods-server】Hystrix服务熔断，Request URL = [{}]，Error Message = [{}]", delegate.getRequest().getURI(), exception.toString());
        return success("系统繁忙，请稍后重试");
    }

    @PostMapping("/pay-server")
    public ResultEntity payServerFallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("【pay-server】Hystrix服务熔断，Request URL = [{}]，Error Message = [{}]", delegate.getRequest().getURI(), exception.toString());
        return success("系统繁忙，请稍后重试");
    }
}

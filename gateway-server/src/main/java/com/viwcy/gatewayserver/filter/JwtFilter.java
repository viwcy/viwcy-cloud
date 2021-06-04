package com.viwcy.gatewayserver.filter;

import com.alibaba.fastjson.JSONObject;
import com.viwcy.gatewayserver.constant.JwtEnum;
import com.viwcy.gatewayserver.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * TODO //
 *
 * <p> Title: JwtFilter </p >
 * <p> Description: JwtFilter </p >
 * <p> History: 2021/4/13 11:04 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtFilter implements GlobalFilter, Ordered {

    private static final String JWT_HEADER_STRING = "Authorization";

    /**
     * jwt放行接口
     */
    private String[] passUrls;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求url
        String requestUrl = exchange.getRequest().getURI().getPath();
        for (String url : passUrls) {
            if (requestUrl.startsWith(url)) {
                return chain.filter(exchange);
            }
        }

        String jwt = exchange.getRequest().getHeaders().getFirst(JWT_HEADER_STRING);
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(jwt)) {
            return response(response, JwtEnum.JWT_MISS.getCode(), JwtEnum.JWT_MISS.getMessage());
        } else {
            //解析jwt
            try {
                jwtUtil.parsingJwt(jwt);
            } catch (ExpiredJwtException e) {
                return response(response, JwtEnum.JWT_EXPIRED.getCode(), JwtEnum.JWT_EXPIRED.getMessage());
            } catch (SignatureException e) {
                return response(response, JwtEnum.JWT_INVALID.getCode(), JwtEnum.JWT_INVALID.getMessage());
            } catch (Exception e) {
                return response(response, JwtEnum.JWT_ERROR.getCode(), JwtEnum.JWT_ERROR.getMessage());
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * jwt过滤响应结果
     */
    private Mono<Void> response(ServerHttpResponse response, int code, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        byte[] bits = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}

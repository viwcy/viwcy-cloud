package com.viwcy.gatewayserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

/**
 * TODO //jwt工具模板
 *
 * <p> Title: JwtTemplate </p>
 * <p> Description: JwtTemplate </p>
 * <p> History: 2020/9/4 23:02 </p>
 * <pre>
 *      Copyright: Create by FQ, ltd. Copyright(©) 2020.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
//@ConfigurationProperties(prefix = "jwt.config")
@Component
public class JwtUtil {

    /**
     * 前缀
     */
    private static final String JWT_PREFIX = "Bearer";

    /**
     * 签名秘钥
     */
    private static final String JWT_SECRET = "viwcy4611";

    /**
     * @param jwt
     * @return io.jsonwebtoken.Claims
     * @Description TODO    解析jwt
     * @date 2020/9/3 17:40
     * @author Fuqiang
     */
    public Claims parsingJwt(String jwt) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt.replace(JWT_PREFIX + " ", "")).getBody();
    }

}

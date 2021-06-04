package com.viwcy.userserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * TODO //
 *
 * <p> Title: SecurityConfiguration </p >
 * <p> Description: SecurityConfiguration </p >
 * <p> History: 2021/4/14 10:33 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Security权限接口，当前项目只用了密码密文存储，所以不需要接口权限，全部放行即可
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略权限授权接口,填写路径就好
        web.ignoring().antMatchers("/**");
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

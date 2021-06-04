package com.viwcy.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 应用于集群服务，搭建Dashboard可视化服务监控平台
 * 1.进入后台地址 localhost:9001/hystrix(9001当前服务端口)
 * 2.页面地址栏输入 localhost:9001/turbine.stream，实现对所配置服务的服务监控
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbine
@EnableHystrixDashboard
public class TurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbineApplication.class, args);
    }

}

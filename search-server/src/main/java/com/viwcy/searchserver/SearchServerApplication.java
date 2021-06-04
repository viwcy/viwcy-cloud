package com.viwcy.searchserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SearchServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchServerApplication.class, args);
    }

}

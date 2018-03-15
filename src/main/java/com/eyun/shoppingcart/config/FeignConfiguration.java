package com.eyun.shoppingcart.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.eyun.shoppingcart")
public class FeignConfiguration {

}

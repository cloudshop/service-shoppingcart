package com.eyun.shoppingcart.config;

import feign.Feign;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableFeignClients(basePackages = "com.eyun.shoppingcart")
public class FeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder(){
        return new MyErrorDecoder();
    }
}

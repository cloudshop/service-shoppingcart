package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.client.AuthorizedFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@AuthorizedFeignClient(name = "user")
public interface FeignShopClient {
    @GetMapping("/api/mercuries/{id}")
    public ResponseEntity<Map<String,String>> getShopById(@PathVariable("id") Long shopId);
}

package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

//@AuthorizedFeignClient()
public interface FeignShopClient {
    //@GetMapping("")
    //public Map getShopById(@PathVariable Long shopId);
}

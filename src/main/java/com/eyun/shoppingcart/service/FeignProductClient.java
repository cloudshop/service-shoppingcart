package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@AuthorizedFeignClient(name="product")
public interface FeignProductClient {
    @GetMapping("/api/sku-imgs/")
    public List<Map> getSkuImg(@RequestParam("skuId") Long skuId);

    @GetMapping("/api/product-skus/{skuId}")
    public Map getSku(@PathVariable("skuId") Long skuId);

}

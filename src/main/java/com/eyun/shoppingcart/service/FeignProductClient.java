package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@AuthorizedFeignClient(name="product")
public interface FeignProductClient {
    @GetMapping("/api/product-skus/{id}")
    public Map getSku(@PathVariable("id") Long id);

    @GetMapping("/skuimgs/{skuId}")
    public List<Map> getSkuImgs(@PathVariable("skuId") Long skuId);

}

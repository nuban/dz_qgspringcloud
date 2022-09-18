package com.example.goods.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "service-store", configuration = FeignOkhttpConfig.class)
public interface StoreClient {

    @GetMapping("/store/{skuID}")
    Map<String, Object> getStoreNum(@PathVariable String skuID);

}

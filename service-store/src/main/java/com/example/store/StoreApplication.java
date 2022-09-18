package com.example.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @RestController
    @RequestMapping("/store")
    class StoreController {

        @Value("${server.port}")
        String port;

        @GetMapping("/{skuID}")
        public Map<String, Object> getStoreNum(@PathVariable String skuID) throws InterruptedException {
            System.out.println("当前正在请求服务：" + port + "，参数：" + skuID);
            Map<String, Object> map = new HashMap<>();
            map.put("port", port);
            map.put("num", 100L);

//            Thread.sleep(50000);
            return map;
        }
    }
}

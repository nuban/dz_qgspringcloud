package com.example.goods;

import com.example.goods.feign.StoreClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Slf4j
@RestController
@RequestMapping("/goods")
@RefreshScope
class GoodsController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${env.name:}")
    String envName;
    @Value("${env.ip:}")
    String envIp;
    @Value("${goods.name:}")
    String goodsName;

    @Autowired
    StoreClient storeClient;

    @GetMapping("/config")
    public String getConfig() {
        return "当前读取到的环境名称：" + envName + ", IP: " + envIp;
    }

    @GetMapping("/config/store")
    public String getStoreConfig() {
        return "当前读取到的商品名称：" + goodsName;
    }

    @GetMapping("/{skuID}")
    public GoodsSkuDTO getGoodsInfo(@PathVariable String skuID) throws JsonProcessingException {
        System.out.println("当前正在请求商品服务，参数：" + skuID);
        GoodsSkuDTO goodsSkuDTO = new GoodsSkuDTO();
        goodsSkuDTO.setSkuNo("123456");
        goodsSkuDTO.setColor("白色");
        goodsSkuDTO.setName("潮流T恤男");
        goodsSkuDTO.setSize("L");
        goodsSkuDTO.setPrice(BigDecimal.valueOf(39.90));
        // 从库存服务拿到商品的库存量
//            String res = restTemplate.getForObject("http://service-store/store/" + skuID, String.class);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map map = objectMapper.readValue(res, Map.class);

        Map<String, Object> map = null;
        log.info("请求库存服务参数：" + skuID);
        long startTimeMS = System.currentTimeMillis();
        try {
            map = storeClient.getStoreNum(skuID);
        } catch (Exception e) {
            log.error("{}请求{}， 参数skuID={}出错", "service-goods", "service-store", skuID, e);
        }
        log.info("请求库存服务返回数据：" + map);
        log.info("请求库存服务消耗的时间：" + (System.currentTimeMillis() - startTimeMS) + "ms");

       if( map != null) {
           Long num = Long.valueOf(map.get("num").toString());
           goodsSkuDTO.setStore(num);

           String port = (String) map.get("port");
           System.out.println("获取到当前库存服务端口：" + port);
       }
        return goodsSkuDTO;
    }
}

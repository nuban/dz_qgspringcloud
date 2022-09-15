package com.example.goods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

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

    @RestController
    @RequestMapping("/goods")
    class GoodsController {

        @Autowired
        RestTemplate restTemplate;

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
            String res = restTemplate.getForObject("http://service-store/store/" + skuID, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(res, Map.class);
            Long num = Long.valueOf(map.get("num").toString());
            goodsSkuDTO.setStore(num);

            String port = (String) map.get("port");
            System.out.println("获取到当前库存服务端口：" + port);
            return goodsSkuDTO;
        }
    }
}

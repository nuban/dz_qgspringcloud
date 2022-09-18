package com.example.goods.feign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "feign.okhttp")
public class OkhttpProperties {
    private Long connectTimeout;
    private Long readTimeout;
}

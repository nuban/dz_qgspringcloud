package com.example.goods.feign;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Configuration
@ConditionalOnClass({OkHttpClient.class})
@ConditionalOnProperty({"feign.okhttp.enabled"})
public class FeignOkhttpConfig {

    @Bean
    public okhttp3.OkHttpClient okHttpClient(OkhttpProperties okhttpProperties) {
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(okhttpProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                //设置读超时
                .readTimeout(okhttpProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                .addInterceptor(new OkHttpLogInterceptor())
                //构建OkHttpClient对象
                .build();
    }
}

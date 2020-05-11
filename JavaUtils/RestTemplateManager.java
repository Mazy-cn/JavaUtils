package com.xxxx.utils;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: RestTemplateManager
 * @Description:
 * @Auther: Mazy
 * @create: 2019-06-24 15:40
 */
@Configuration
public class RestTemplateManager {
    @Resource
    private LoadBalancerInterceptor loadBalancerInterceptor;

    /**
     * Time:millisecond
     * @param readtimout
     * @param connectionRequestTimeout
     * @param connectTimeout
     * @return HttpComponentsClientHttpRequestFactory
     */
    public HttpComponentsClientHttpRequestFactory getHttpRequestFactory(int readtimout,int connectionRequestTimeout,int connectTimeout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readtimout);
        factory.setConnectionRequestTimeout(connectionRequestTimeout);
        factory.setConnectTimeout(connectTimeout);
        factory.setBufferRequestBody(false);
        return factory;
    }

    @Bean
    @Scope("prototype")
    public RestTemplate getConfigRestTemplate(HttpComponentsClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate();
        //解决编码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(factory);
        //add loadbalancerInterceptor,list is empty default
        restTemplate.getInterceptors().add(loadBalancerInterceptor);
        return restTemplate;
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //解决编码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}

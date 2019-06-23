package com.rule;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lindj
 * @date 2019/6/20 0020
 * @description
 */
//@Configuration
public class MyRule {
    /**
     * 使用LoadBalanced开启客户端负载均衡的功能
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IRule getMyRule(){
        return new MyRoundRobinRule();
    }
}

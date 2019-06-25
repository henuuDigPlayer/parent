package com.data4truth.pi.config.bean;

import com.data4truth.pi.config.RibbonRuleEnvironmentConfig;
import com.data4truth.pi.interceptor.FeignRequestInterceptor;
import com.data4truth.pi.interceptor.RestTemplateRequestInterceptor;
import feign.Feign;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.DefaultPropertiesFactory;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description
 */
@Configuration
public class BeanConfig {

    @Bean
    public PropertiesFactory defaultPropertiesFactory() {
        return new DefaultPropertiesFactory();
    }

    @Bean
    public RibbonRuleEnvironmentConfig ribbonLoadbalancerRuleConfiguration() {
        return new RibbonRuleEnvironmentConfig();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new RestTemplateRequestInterceptor());
        return restTemplate;
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new FeignRequestInterceptor());
    }
}

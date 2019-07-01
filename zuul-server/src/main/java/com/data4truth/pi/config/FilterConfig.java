package com.data4truth.pi.config;

import com.data4truth.pi.filter.GrayFilter;
import com.data4truth.pi.filter.GrayPostZuulFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lindj
 * @date 2019/7/1 0001
 * @description
 */
@Configuration
public class FilterConfig {

    @Bean
    public GrayFilter grayFilter() {
        return new GrayFilter();
    }

    @Bean
    public GrayPostZuulFilter grayPostFilter() {
        return new GrayPostZuulFilter();
    }
}

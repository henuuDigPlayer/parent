package com.data4truth.pi;

import com.data4truth.pi.filter.GrayFilter;
import com.data4truth.pi.filter.GrayPostZuulFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author lindj
 * @date 2019/6/13 0013
 * @description
 */
@EnableEurekaClient
@SpringBootApplication
@EnableZuulProxy
@MapperScan("com.data4truth.pi.mapper")
public class Application {


    @Bean
    public GrayFilter grayFilter() {
        return new GrayFilter();
    }

    @Bean
    public GrayPostZuulFilter grayPostFilter() {
        return new GrayPostZuulFilter();
    }
    public static void main( String[] args ) {
        SpringApplication.run(Application.class, args);
    }

}
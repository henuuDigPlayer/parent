package com.data4truth.pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author lindj
 * @date 2019/6/13 0013
 * @description
 */
@EnableEurekaClient
@SpringBootApplication
@EnableZuulProxy
public class Application {

    public static void main( String[] args ) {
        SpringApplication.run(Application.class, args);
    }

}
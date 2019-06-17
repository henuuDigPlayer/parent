package com.data4truth.pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author lindj
 * @date 2019/6/13 0013
 * @description
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {
    public static void main( String[] args ) {
        SpringApplication.run(Application.class, args);
    }
}

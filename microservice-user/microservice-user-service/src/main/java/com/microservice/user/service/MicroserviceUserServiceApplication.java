package com.microservice.user.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.microservice.user.service", "com.microservice.common"})
@MapperScan("com.microservice.user.service.mapper")
@EnableDiscoveryClient
public class MicroserviceUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceUserServiceApplication.class, args);
	}

}

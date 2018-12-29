package com.springcloud.eshop.data.link.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DataLinkServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataLinkServerApplication.class, args);
	}

}


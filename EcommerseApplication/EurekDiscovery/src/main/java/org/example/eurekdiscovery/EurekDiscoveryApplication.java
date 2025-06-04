package org.example.eurekdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekDiscoveryApplication.class, args);
	}

}

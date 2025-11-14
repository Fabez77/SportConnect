package com.sportconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.sportconnect.auth.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SportconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportconnectApplication.class, args);
	}

}

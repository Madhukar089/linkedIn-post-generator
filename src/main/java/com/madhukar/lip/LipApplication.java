package com.madhukar.lip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LipApplication {

	public static void main(String[] args) {
		SpringApplication.run(LipApplication.class, args);
	}

}

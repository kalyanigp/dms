package com.ecomm.define;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ecomm.define"})
public class DefineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefineApplication.class, args);
	}

}

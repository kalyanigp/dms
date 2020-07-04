package com.dms.defineconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigServer
public class DefineConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefineConfigServerApplication.class, args);
	}

}

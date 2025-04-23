package com.vaibavshri.privilege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PrivilegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrivilegeApplication.class, args);
	}

}

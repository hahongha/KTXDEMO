package com.utc.dormitory_managing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class DormitoryManagingApplication {
	public static void main(String[] args) {
		SpringApplication.run(DormitoryManagingApplication.class, args);
	}
}

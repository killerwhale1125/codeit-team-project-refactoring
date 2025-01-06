package com.gathering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 기존 프로젝트 리팩토링
 */
@EnableScheduling
@SpringBootApplication
public class GatheringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatheringApplication.class, args);
	}

}

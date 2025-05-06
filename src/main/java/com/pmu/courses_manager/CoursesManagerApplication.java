package com.pmu.courses_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoursesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursesManagerApplication.class, args);
	}

}

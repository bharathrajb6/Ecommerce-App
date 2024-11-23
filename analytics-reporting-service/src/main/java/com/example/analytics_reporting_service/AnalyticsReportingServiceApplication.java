package com.example.analytics_reporting_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnalyticsReportingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsReportingServiceApplication.class, args);
	}

}

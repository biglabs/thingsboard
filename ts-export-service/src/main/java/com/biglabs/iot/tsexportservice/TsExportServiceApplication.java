package com.biglabs.iot.tsexportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootConfiguration
@EnableAsync
@ComponentScan({"org.thingsboard.server", "com.biglabs.iot"})
public class TsExportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TsExportServiceApplication.class, args);
	}
}

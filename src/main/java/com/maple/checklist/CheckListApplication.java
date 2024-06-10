package com.maple.checklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@ConfigurationPropertiesScan(basePackages = {"com.maple.checklist.batch", "com.maple.checklist.domain"})
//@ComponentScan(basePackages = {"com.maple.checklist.batch", "com.maple.checklist.domain"})
public class CheckListApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckListApplication.class, args);
    }

}

package com.team3.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.team3") // 엔티티가 위치한 패키지
@EnableJpaRepositories(basePackages = "com.team3")
public class Team3BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(Team3BatchApplication.class, args);
    }
}

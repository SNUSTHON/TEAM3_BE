package com.team3.batch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("test")
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "com.team3")
@EnableJpaRepositories(basePackages = "com.team3")
public class TestBatchConfig {

}

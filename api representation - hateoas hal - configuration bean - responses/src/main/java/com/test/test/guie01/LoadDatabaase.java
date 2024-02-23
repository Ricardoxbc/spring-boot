package com.test.test.guie01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabaase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabaase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Inserted employee: " + repository.save(new Employee("Ricardo", "admin")));
            log.info("Inserted employee: " + repository.save(new Employee("Maria", "customer")));
        };
    }
}

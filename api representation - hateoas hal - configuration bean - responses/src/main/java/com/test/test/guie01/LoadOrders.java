package com.test.test.guie01;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadOrders {
    @Bean
    CommandLineRunner initOrders(OrderRepository repository) {
        return args -> {
            repository.save(new Order("Order 1", Order.Status.IN_PRO));
            repository.save(new Order("Order 2", Order.Status.IN_PRO));
        };
    }
}

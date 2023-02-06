package org.springframework.guides.rest.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.guides.rest.payroll.employee.Employee;
import org.springframework.guides.rest.payroll.employee.EmployeeRepository;
import org.springframework.guides.rest.payroll.order.Order;
import org.springframework.guides.rest.payroll.order.OrderRepository;
import org.springframework.guides.rest.payroll.order.Status;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
          log.info("Preloading " + employeeRepository.save(new Employee("Bilblo Baggins", "burglar")));
          log.info("Preloading " + employeeRepository.save(new Employee("Frodo Baggins", "thief")));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));
            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });

        };
    }
}

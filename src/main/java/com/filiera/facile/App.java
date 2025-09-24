package com.filiera.facile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.filiera.facile.repositories")
public class App {
    public static void main(String[] args) {

        System.out.println("===============================");
        System.out.println("Spring Boot Application Started");
        System.out.println("===============================");
        SpringApplication.run(App.class, args);

    }
}

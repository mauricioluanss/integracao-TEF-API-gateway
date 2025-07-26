package com.apigateway.apigateway.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Menu menu = context.getBean(Menu.class);
        menu.menuPrincipal();
    }
}
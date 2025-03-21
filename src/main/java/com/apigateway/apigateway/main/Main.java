package com.apigateway.apigateway.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        /* `Context` starta a aplicação.
         * `menuDeOpcoes` inicia paralelamente o menu de interação.*/
        ApplicationContext context = SpringApplication.run(Main.class, args);
        MenusDeOpcoes menusDeOpcoes = context.getBean(MenusDeOpcoes.class);
        menusDeOpcoes.menuPrincipal();
    }
}
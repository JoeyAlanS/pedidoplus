package org.example.pedidoplus;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class PedidoplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidoplusApplication.class, args);
    }

}

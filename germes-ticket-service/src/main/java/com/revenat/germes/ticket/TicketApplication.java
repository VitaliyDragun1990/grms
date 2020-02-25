package com.revenat.germes.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Vitaliy Dragun
 */
@SpringBootApplication(scanBasePackages = "com.revenat.germes.ticket")
public class TicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
}

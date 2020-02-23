package com.revenat.germes.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Vitaliy Dragun
 */
@SpringBootApplication(scanBasePackages = "com.revenat.germes.user")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}

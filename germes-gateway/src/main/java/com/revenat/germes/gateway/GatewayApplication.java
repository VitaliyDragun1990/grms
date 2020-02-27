package com.revenat.germes.gateway;

import com.revenat.germes.gateway.application.security.token.jwt.JwtProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Vitaliy Dragun
 */
@SpringBootApplication(scanBasePackages = "com.revenat.germes.gateway")
@EnableConfigurationProperties(JwtProcessor.class)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

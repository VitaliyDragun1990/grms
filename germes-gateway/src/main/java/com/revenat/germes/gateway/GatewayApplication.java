package com.revenat.germes.gateway;

import com.revenat.germes.gateway.domain.model.route.DynamicRouteProvider;
import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import com.revenat.germes.gateway.domain.model.token.jwt.JwtProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @author Vitaliy Dragun
 */
@SpringBootApplication(scanBasePackages = "com.revenat.germes.gateway")
@EnableConfigurationProperties({JwtProcessor.class, DynamicRouteProvider.class})
public class GatewayApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

    @Autowired
    private TokenProcessor tokenProcessor;

    @PostConstruct
    private void printToken() {
        LOGGER.info("*******AUTH TOKEN={}",tokenProcessor.generateToken("test"));
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

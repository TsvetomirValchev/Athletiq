package com.valchev.athletiq;

import com.valchev.athletiq.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class})
public class AthletiqApplication {

    public static void main(String[] args) {
        SpringApplication.run(AthletiqApplication.class, args);
    }

}

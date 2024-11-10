package com.example.daydreamer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayosConfig {

    @Value("${spring.application.security.payos.payos_client_id}")
    private String clientId;

    @Value("${spring.application.security.payos.payos_api_key}")
    private String apiKey;

    @Value("${spring.application.security.payos.payos_checksum_key}")
    private String checksumKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}

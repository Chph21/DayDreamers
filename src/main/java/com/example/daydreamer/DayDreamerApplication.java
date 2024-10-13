package com.example.daydreamer;

import com.example.daydreamer.entity.Account;
import com.example.daydreamer.entity.AuthEntity;
import com.example.daydreamer.enums.AccountRole;
import com.example.daydreamer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DayDreamerApplication {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(DayDreamerApplication.class, args);
    }

    @Bean
    public ApplicationRunner checkAndCreateAdmin() {
        return args -> {
            if (accountRepository.findByUsername("admin") == null) {
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin")); // Ensure the password is encoded
                admin.setAuth(
                        AuthEntity.builder()
                                .role(AccountRole.ADMIN)
                                .isEnable(true)
                                .isBlocked(false)
                                .build());
                accountRepository.save(admin);
            }
        };
    }

}

package com.example.daydreamer;

import com.example.daydreamer.entity.Account;
import com.example.daydreamer.entity.AuthEntity;
import com.example.daydreamer.enums.AccountRole;
import com.example.daydreamer.repository.AccountRepository;
import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // in case .env file is not found
                .load();

        // Manually set environment variables if needed
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        SpringApplication.run(DayDreamerApplication.class, args);
    }

    @Bean
    public ApplicationRunner checkAndCreateAdmin() {
        return args -> {
            if (accountRepository.findByUsername("admin") == null) {
                Account admin = new Account();
                admin.setPhoneNumber("+84123456789");
                admin.setUsername("admin");
                admin.setFullName("Admin");
                admin.setNationality("Vietnam");
                admin.setStatus("Active");
                // Ensure the password is encoded
                AuthEntity auth = new AuthEntity();
                auth.setPassword(passwordEncoder.encode("admin"));
                auth.setRole(AccountRole.ADMIN);
                auth.setPhone("+84123456789");
                auth.setEmail("admin@gmail.com");
                auth.setIsEnable(true);
                auth.setIsBlocked(false);
                auth.setAccount(admin);
                admin.setAuth(auth);
                accountRepository.save(admin);
            }
        };
    }

}

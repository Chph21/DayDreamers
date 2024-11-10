package com.example.daydreamer.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class OtpToken {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;
    @Column(unique = true)
    private String userId;
    private String otpSecret;
    @CreationTimestamp
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;
}

package com.example.daydreamer.entity;

import com.example.daydreamer.enums.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity implements UserDetails {

    @Id
    @Column(name = "phone", length = 50, nullable = false)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_enable", nullable = false)
    private Boolean isEnable;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}

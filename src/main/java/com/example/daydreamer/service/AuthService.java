package com.example.daydreamer.service;

import com.example.daydreamer.entity.Account;
import com.example.daydreamer.entity.AuthEntity;
import com.example.daydreamer.entity.OtpToken;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.enums.AccountRole;
import com.example.daydreamer.enums.TokenType;
import com.example.daydreamer.model._RequestModel.AuthenticationRequest;
import com.example.daydreamer.model._RequestModel.RegisterRequest;
import com.example.daydreamer.model._RequestModel.MailOTPRequest;
import com.example.daydreamer.model._ResponseModel.AuthenticationResponse;
import com.example.daydreamer.model._ResponseModel.RegisterResponse;
import com.example.daydreamer.model._ResponseModel.MailOTPResponse;
import com.example.daydreamer.repository.AccountRepository;
import com.example.daydreamer.repository.AuthRepository;
import com.example.daydreamer.repository.OtpTokenRepository;
import com.example.daydreamer.repository.StudioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler {

    @Value("${spring.application.security.sms-otp.twilio-account-sid}")
    private String accountSid;
    @Value("${spring.application.security.sms-otp.twilio-auth-token}")
    private String authToken;
    @Value("${spring.application.security.sms-otp.twilio-service-sid}")
    private String serviceSid;
    private static final int OTP_LENGTH = 6;
    private static final long EXPIRATION_MINUTES = 3;

    private final AuthenticationManager authenticationManager;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final StudioRepository studioRepository;
    private final OtpTokenRepository otpTokenRepository;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AuthEntity authEntity = authRepository.findByEmail(request.getEmail()).orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.generateAccessToken(extraClaims, authEntity.getEmail(), String.valueOf(authEntity.getRole()));
        String refreshToken = jwtService.generateRefreshToken(authEntity.getEmail());
        // Update the refresh token in the database
        authEntity.setRefreshToken(refreshToken);
        authRepository.save(authEntity);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public RegisterResponse register(RegisterRequest request) {
        Studio studio = null;
        if(request.getStudioId() != null) {
            studio = studioRepository.findById(request.getStudioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Studio not found"));
        }
        Account account = new Account();
        if(studio != null) {
             account = Account.builder()
                    .studio(studio)
                    .phoneNumber(request.getPhone())
                    .username(request.getEmail())
                    .fullName(request.getFullName())
                    .address(request.getAddress())
                    .gender(request.getGender())
                    .instagram(request.getInstagram())
                    .dob(request.getDob())
                    .nationality(request.getNationality())
                    .status(request.getStatus())
                    .build();
        }else {
             account = Account.builder()
                    .phoneNumber(request.getPhone())
                    .username(request.getEmail())
                    .fullName(request.getFullName())
                    .address(request.getAddress())
                    .gender(request.getGender())
                    .instagram(request.getInstagram())
                    .dob(request.getDob())
                    .nationality(request.getNationality())
                    .status(request.getStatus())
                    .build();
        }

        AuthEntity auth = AuthEntity.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountRole.USER)
                .isEnable(false)
                .isBlocked(false)
                .account(account)
                .build();

        account.setAuth(auth);

        Account savedAccount = accountRepository.save(account);
        authRepository.save(auth);

        return RegisterResponse.builder()
                .id(savedAccount.getId())
                .email(savedAccount.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authenticationHeader = request.getHeader("Authorization");
        final String refreshToken;

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing");
        }
        refreshToken = authenticationHeader.replace("Bearer ", "");
        final AuthEntity authEntity = authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (jwtService.isTokenValid(refreshToken, authEntity, TokenType.REFRESH)) {
            Map<String, Object> extraClaims = new HashMap<>();
            String newAccessToken = jwtService.generateAccessToken(extraClaims, authEntity.getPhone(), String.valueOf(authEntity.getRole()));
            System.out.println("before return");
            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new RuntimeException("Refresh token is invalid");
    }

    private static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public MailOTPResponse generateOtp(MailOTPRequest mailOTPRequest) {
        Account user = accountRepository.findByUsername(mailOTPRequest.getEmail());
        if (user != null) {
            // Generate a random OTP
            String otp = generateOTP();
            OtpToken otpToken = otpTokenRepository.findByUserId(user.getId());
            if (otpToken != null) {
                otpTokenRepository.delete(otpToken);
            }
            OtpToken newOtpToken = new OtpToken();
            newOtpToken.setUserId(user.getId());
            newOtpToken.setOtpSecret(otp);
            newOtpToken.setCreationTime(LocalDateTime.now());
            newOtpToken.setExpirationTime(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
            otpTokenRepository.save(newOtpToken);
            emailService.sendOTPByEmail(mailOTPRequest.getEmail(), otp);
            return MailOTPResponse.builder().email(mailOTPRequest.getEmail()).status("SUCCESS").build();
        }
        return MailOTPResponse.builder().email(mailOTPRequest.getEmail()).status("FAIL").build();
    }

    public MailOTPResponse verifyUserOTP(MailOTPRequest mailOTPRequest) {
        Account user = accountRepository.findByUsername(mailOTPRequest.getEmail());
        OtpToken otpToken = otpTokenRepository.findByUserId(user.getId());
        if (otpToken != null) {
            String storedOTP = otpToken.getOtpSecret();
            LocalDateTime expirationTime = otpToken.getExpirationTime();
            if(mailOTPRequest.getOtp().equals(storedOTP)){
                if (LocalDateTime.now().isBefore(expirationTime)) {
                    otpTokenRepository.deleteByUserId(user.getId());
                    return MailOTPResponse.builder()
                            .email(mailOTPRequest.getEmail())
                            .status("VERIFIED")
                            .build();
                }
                return MailOTPResponse.builder()
                        .email(mailOTPRequest.getEmail())
                        .status("EXPIRED")
                        .build();
            }
        }
        return MailOTPResponse.builder()
                .email(mailOTPRequest.getEmail())
                .status("INVALID")
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    private void deleteExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        otpTokenRepository.deleteExpiredTokens(currentDateTime);
    }
}

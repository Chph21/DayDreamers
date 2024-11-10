package com.example.daydreamer.controller;

import com.example.daydreamer.model._RequestModel.AuthenticationRequest;
import com.example.daydreamer.model._RequestModel.RegisterRequest;
import com.example.daydreamer.model._RequestModel.MailOTPRequest;
import com.example.daydreamer.model._ResponseModel.AuthenticationResponse;
import com.example.daydreamer.model._ResponseModel.RegisterResponse;
import com.example.daydreamer.model._ResponseModel.MailOTPResponse;
import com.example.daydreamer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        AuthenticationResponse refreshTokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(refreshTokenResponse);
    }

    @PostMapping(value = "/send-otp")
    public ResponseEntity<MailOTPResponse> generateOtp(@Valid @RequestBody MailOTPRequest mailOTPRequest) {
        MailOTPResponse response = authService.generateOtp(mailOTPRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MailOTPResponse> verifyUserOTP(@RequestBody MailOTPRequest mailOTPRequest) {
        MailOTPResponse response = authService.verifyUserOTP(mailOTPRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-authentication")
    public ResponseEntity<String> testAuthentication() {
        return ResponseEntity.ok("Authentication successful");
    }

}

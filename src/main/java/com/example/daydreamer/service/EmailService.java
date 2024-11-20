package com.example.daydreamer.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendOTPByEmail(String userEmail, String otp) throws MessagingException, IOException {
        String subject = "OTP for Verification";

        // Read the HTML template from the resources folder
        ClassPathResource resource = new ClassPathResource("otp-verification-template.html");
        String body = new String(Files.readAllBytes(Paths.get(resource.getURI())), "UTF-8");

        // Replace placeholders with actual values
        body = body.replace("${otp}", otp);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(userEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        javaMailSender.send(message);
    }

//    @Async
//    public void sendWelcomeEmail(String userEmail, String name, String password) throws MessagingException {
//        String subject = "Welcome to Our Website!";
//        String body = "<html><body style='font-family: Arial, sans-serif;'>"
//                + "<h2 style='color: #007bff;'>Welcome to Our Website, " + name + "!</h2>"
//                + "<p>We are thrilled to welcome you to our website!</p>"
//                + "<p>Your account has been successfully created. Below are your login credentials:</p>"
//                + "<ul>"
//                + "<li><strong>Username:</strong> " + userEmail + "</li>"
//                + "<li><strong>Password:</strong> " + password + "</li>"
//                + "</ul>"
//                + "<p>Please keep your login credentials secure and do not share them with anyone.</p>"
//                + "<p>Thank you for choosing our services. We hope you enjoy your experience on our website!</p>"
//                + "<p style='color: #28a745;'>Best regards,<br/>Your Website Team</p>"
//                + "</body></html>";
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setTo(userEmail);
//        helper.setSubject(subject);
//        helper.setText(body, true);
//        javaMailSender.send(message);
//    }


}

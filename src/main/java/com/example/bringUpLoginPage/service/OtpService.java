package com.example.bringUpLoginPage.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9._%+-]*@(gmail\\.com|yahoo\\.com)$");

    private final Map<String, Integer> otpStorage = new HashMap<>();
    private final Random random = new Random();

    public int generateOtp() {
        return 100000 + random.nextInt(900000); // Generates a 6-digit OTP
    }

    public String sendOtp(String email) {

        boolean isValidEmail = EMAIL_PATTERN.matcher(email).matches();

        System.out.println("Email validation result: " + isValidEmail + " " + email);

        if (!isValidEmail) {
            throw new IllegalArgumentException("Invalid email! Only @gmail.com or @yahoo.com allowed.");
        }
        int otp = generateOtp();
        otpStorage.put(email, otp);
        try {
            sendOtpEmail(email, otp);
            return "OTP sent successfully to " + email;
        } catch (MessagingException e) {
            return "Error sending OTP: " + e.getMessage();
        }
    }

    private void sendOtpEmail(String toEmail, int otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP code is: " + otp, true);
        helper.setFrom("your-email@gmail.com");

        mailSender.send(message);
    }

    public boolean verifyOtpEmail(String email, int otp) {

        return otpStorage.containsKey(email) && otpStorage.get(email) == otp;
    }
}
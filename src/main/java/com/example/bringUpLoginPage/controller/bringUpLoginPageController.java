package com.example.bringUpLoginPage.controller;

import com.example.bringUpLoginPage.service.bringUpLoginPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/user")
public class bringUpLoginPageController {

    private final bringUpLoginPageService loginService;

    public bringUpLoginPageController(bringUpLoginPageService loginService) {
        this.loginService = loginService;
    }

    // Send OTP
    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String message = loginService.sendOtp(email);
        return ResponseEntity.ok(message);
    }


    // Verify OTP and Register
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam int otp
    ) {
        String result = loginService.verifyOtpAndRegisterUser(email, otp);
        return ResponseEntity.ok(result);
    }

    // Get user by email
    @GetMapping("/getUser")
    public ResponseEntity<?> getUserByEmail(@RequestParam String emailID) {
        return loginService.findByEmailID(emailID);
    }
}

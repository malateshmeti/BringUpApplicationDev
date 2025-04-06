package com.example.bringUpLoginPage.service;

import com.example.bringUpLoginPage.model.bringUpLoginPageModel;
import com.example.bringUpLoginPage.repository.bringUpLoginPageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.bringUpLoginPage.service.OtpService.EMAIL_PATTERN;

@Service
public class bringUpLoginPageService {

    private final bringUpLoginPageRepository userInfo;
    private final OtpService otpService;

    public bringUpLoginPageService(bringUpLoginPageRepository userInformation, OtpService otpService) {
        this.userInfo = userInformation;
        this.otpService = otpService;
    }

    public String verifyOtpAndRegisterUser(String email, int otp) {


        if (otpService.verifyOtpEmail(email, otp)) {
            Optional<bringUpLoginPageModel> existingUser = userInfo.findByEmailID(email);

            if (existingUser.isPresent()) {
                return "Email already registered, SignIn!";
            }

            bringUpLoginPageModel newUser = new bringUpLoginPageModel();
            newUser.setEmailID(email);

            userInfo.save(newUser);

            return "OTP verified! User registered successfully";
        }
        return "Invalid OTP. Please try again.";
    }

    public String sendOtp(String email) {

        return otpService.sendOtp(email);
    }

    public bringUpLoginPageModel addUser(bringUpLoginPageModel user) {

        System.out.println("Received User: " + user);
        System.out.println("Email received: " + user.getEmailID());

        // Validate email format before saving
        boolean isValidEmail = EMAIL_PATTERN.matcher(user.getEmailID()).matches();

        System.out.println("Email validation result: " + isValidEmail + " " + user.getEmailID());

        if (!isValidEmail) {
            throw new IllegalArgumentException("Invalid email! Only @gmail.com or @yahoo.com allowed.");
        }

        return userInfo.save(user);
    }

    public void deleteUser(String emailID) {
        userInfo.deleteById(emailID);
    }
    //findAll()
    public List<bringUpLoginPageModel> getAllUsers() {
        List<bringUpLoginPageModel> users = userInfo.findAll();
        return users;
    }

    public ResponseEntity<?> findByEmailID(String emailID) {
        // Validate email format
        if (!EMAIL_PATTERN.matcher(emailID).matches()) {
            return ResponseEntity.badRequest().body("Invalid email! Only @gmail.com or @yahoo.com allowed.");
        }

        // Check if email exists in the database
        Optional<bringUpLoginPageModel> user = userInfo.findByEmailID(emailID);

        if (user.isPresent()) {
            System.out.println("Response1" + ResponseEntity.ok(user.get()));
            return ResponseEntity.ok(user.get()); // Return user details if found
        } else {
            System.out.println("Response1" + " " + ResponseEntity.badRequest().body("New User"));

            return ResponseEntity.badRequest().body("New User"); // Return error message if not found
        }
    }

}
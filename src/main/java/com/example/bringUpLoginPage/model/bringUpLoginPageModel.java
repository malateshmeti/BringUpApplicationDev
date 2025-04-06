package com.example.bringUpLoginPage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "t_User") // Ensure it matches your MySQL table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class bringUpLoginPageModel {

    @Id
    @Column(name = "emailid", nullable = false, unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format") // Ensures email format is valid
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9._%+-]*@(gmail\\.com|yahoo\\.com)$",
            message = "Email must start with a letter and be a valid Gmail or Yahoo address")
    private String emailID;

    @Column(name = "Isverified", nullable = false)
    private int Isverified = 1;

    public CharSequence getEmailID() {
        return emailID;
    }

    public void setEmailID(String email) {
        this.emailID = email;
    }
}

package com.example.bringUpLoginPage.repository;

import com.example.bringUpLoginPage.model.bringUpLoginPageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface bringUpLoginPageRepository extends JpaRepository<bringUpLoginPageModel, String> {

    Optional<bringUpLoginPageModel> findByEmailID(String emailID);
}

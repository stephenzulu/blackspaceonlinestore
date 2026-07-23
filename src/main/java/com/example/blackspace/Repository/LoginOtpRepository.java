package com.example.blackspace.Repository;

import com.example.blackspace.Model.LoginOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginOtpRepository extends JpaRepository<LoginOtp, Long> {
    Optional<LoginOtp> findTopByEmailAndUsedFalseOrderByExpiryDateDesc(String email);
    void deleteByEmail(String email);
}

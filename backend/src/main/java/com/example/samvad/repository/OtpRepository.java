package com.example.samvad.repository;

import com.example.samvad.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByPhoneNumberAndIsVerifiedFalseOrderByCreatedAtDesc(String phoneNumber);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}


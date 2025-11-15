package com.example.samvad.service;

import com.example.samvad.entity.OtpVerification;
import com.example.samvad.entity.User;
import com.example.samvad.repository.OtpRepository;
import com.example.samvad.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public void sendOtp(String phoneNumber) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));

        // Save OTP to database
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setPhoneNumber(phoneNumber);
        otpVerification.setOtp(otp);
        otpRepository.save(otpVerification);

        // Send OTP via Twilio
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your Samvad verification code is: " + otp
            ).create();

            log.info("OTP sent successfully to {}", phoneNumber);
        } catch (Exception e) {
            log.error("Failed to send OTP: {}", e.getMessage());
            // For development, you can log the OTP
            log.info("OTP for {}: {}", phoneNumber, otp);
        }
    }

    @Transactional
    public boolean verifyOtp(String phoneNumber, String otp) {
        Optional<OtpVerification> otpVerificationOpt =
            otpRepository.findTopByPhoneNumberAndIsVerifiedFalseOrderByCreatedAtDesc(phoneNumber);

        if (otpVerificationOpt.isEmpty()) {
            log.error("No OTP found for phone number: {}", phoneNumber);
            return false;
        }

        OtpVerification otpVerification = otpVerificationOpt.get();

        if (otpVerification.isExpired()) {
            log.error("OTP expired for phone number: {}", phoneNumber);
            return false;
        }

        if (!otpVerification.getOtp().equals(otp)) {
            log.error("Invalid OTP for phone number: {}", phoneNumber);
            return false;
        }

        // Mark OTP as verified
        otpVerification.setVerified(true);
        otpRepository.save(otpVerification);

        // Create user if doesn't exist
        if (!userRepository.existsByPhoneNumber(phoneNumber)) {
            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            newUser.setName("");
            newUser.setStatus("Hey there! I am using Samvad.");
            userRepository.save(newUser);
            log.info("New user created with phone number: {}", phoneNumber);
        }

        return true;
    }
}


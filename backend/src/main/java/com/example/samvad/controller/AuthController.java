package com.example.samvad.controller;

import com.example.samvad.dto.AuthResponse;
import com.example.samvad.dto.SendOtpRequest;
import com.example.samvad.dto.UserDto;
import com.example.samvad.dto.VerifyOtpRequest;
import com.example.samvad.entity.User;
import com.example.samvad.service.OtpService;
import com.example.samvad.service.UserService;
import com.example.samvad.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final OtpService otpService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody SendOtpRequest request) {
        try {
            otpService.sendOtp(request.getPhoneNumber());
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully");
            response.put("phoneNumber", request.getPhoneNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending OTP: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send OTP");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());

            if (isValid) {
                User user = userService.findByPhoneNumber(request.getPhoneNumber())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtUtil.generateToken(user.getPhoneNumber(), user.getUserId());
                String refreshToken = jwtUtil.generateRefreshToken(user.getPhoneNumber(), user.getUserId());

                UserDto userDto = userService.convertToDto(user);
                AuthResponse response = new AuthResponse(token, refreshToken, userDto);

                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired OTP");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Verification failed");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String refreshToken = authHeader.substring(7);
            String phoneNumber = jwtUtil.extractPhoneNumber(refreshToken);
            String userId = jwtUtil.extractUserId(refreshToken);

            if (jwtUtil.validateToken(refreshToken, phoneNumber)) {
                String newToken = jwtUtil.generateToken(phoneNumber, userId);
                String newRefreshToken = jwtUtil.generateRefreshToken(phoneNumber, userId);

                User user = userService.findByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                UserDto userDto = userService.convertToDto(user);
                AuthResponse response = new AuthResponse(newToken, newRefreshToken, userDto);

                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid refresh token");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token refresh failed");
            return ResponseEntity.badRequest().body(error);
        }
    }
}


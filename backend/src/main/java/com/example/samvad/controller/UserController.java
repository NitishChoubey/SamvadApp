package com.example.samvad.controller;

import com.example.samvad.dto.UpdateProfileRequest;
import com.example.samvad.dto.UserDto;
import com.example.samvad.entity.User;
import com.example.samvad.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            log.error("Error getting profile: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get profile");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            log.error("Error getting user profile: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get user profile");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(HttpServletRequest request,
                                          @RequestBody UpdateProfileRequest updateRequest) {
        try {
            String userId = (String) request.getAttribute("userId");
            UserDto userDto = userService.updateProfile(userId, updateRequest);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update profile");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(HttpServletRequest request,
                                               @RequestParam("image") MultipartFile file) {
        try {
            String userId = (String) request.getAttribute("userId");
            String filename = userService.uploadProfileImage(userId, file);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile image uploaded successfully");
            response.put("imageUrl", "/api/users/profile/image/" + userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error uploading profile image: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload profile image");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/profile/image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String userId) {
        try {
            byte[] image = userService.getProfileImage(userId);
            if (image != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting profile image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            log.info("Searching for user with phone number: {}", phoneNumber);

            // Normalize phone number (remove spaces, dashes, etc.)
            String normalizedPhone = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

            User user = userService.findByPhoneNumber(normalizedPhone)
                    .orElse(null);

            if (user != null) {
                UserDto userDto = userService.convertToDto(user);
                log.info("User found: {}", userDto.getUserId());
                return ResponseEntity.ok(userDto);
            } else {
                log.info("User not found with phone number: {}", normalizedPhone);
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            log.error("Error searching user: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to search user");
            return ResponseEntity.badRequest().body(error);
        }
    }
}


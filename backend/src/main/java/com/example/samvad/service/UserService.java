package com.example.samvad.service;

import com.example.samvad.dto.UpdateProfileRequest;
import com.example.samvad.dto.UserDto;
import com.example.samvad.entity.User;
import com.example.samvad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Value("${image.storage.path}")
    private String imageStoragePath;

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public UserDto updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setStatus(request.getStatus());
        User updatedUser = userRepository.save(user);

        return convertToDto(updatedUser);
    }

    @Transactional
    public String uploadProfileImage(String userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create directory if not exists
        Path uploadPath = Paths.get(imageStoragePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : ".jpg";
        String filename = userId + "_" + UUID.randomUUID() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update user profile image path
        user.setProfileImagePath(filename);
        userRepository.save(user);

        log.info("Profile image uploaded for user: {}", userId);
        return filename;
    }

    public byte[] getProfileImage(String userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImagePath() == null) {
            return null;
        }

        Path imagePath = Paths.get(imageStoragePath).resolve(user.getProfileImagePath());
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        }

        return null;
    }

    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setName(user.getName());
        dto.setStatus(user.getStatus());
        if (user.getProfileImagePath() != null) {
            dto.setProfileImageUrl("/api/users/profile/image/" + user.getUserId());
        }
        return dto;
    }
}


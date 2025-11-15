package com.example.samvad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String phoneNumber;
    private String name;
    private String status;
    private String profileImageUrl;
}


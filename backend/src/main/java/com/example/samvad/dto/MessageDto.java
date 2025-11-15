package com.example.samvad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageContent;
    private Long timestamp;
    private String messageType;
    private boolean isDelivered;
    private boolean isRead;
}


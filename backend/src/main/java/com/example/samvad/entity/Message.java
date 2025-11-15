package com.example.samvad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false)
    private String receiverId;

    @Column(nullable = false, length = 5000)
    private String messageContent;

    @Column(nullable = false)
    private Long timestamp;

    @Enumerated(EnumType.STRING)
    private MessageType messageType = MessageType.TEXT;

    private boolean isDelivered = false;

    private boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        timestamp = System.currentTimeMillis();
    }

    public enum MessageType {
        TEXT,
        IMAGE,
        VIDEO,
        AUDIO,
        DOCUMENT
    }
}


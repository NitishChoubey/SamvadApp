package com.example.samvad.controller;

import com.example.samvad.dto.MessageDto;
import com.example.samvad.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<MessageDto>> getConversation(HttpServletRequest request,
                                                            @PathVariable String otherUserId) {
        try {
            String userId = (String) request.getAttribute("userId");
            List<MessageDto> messages = messageService.getConversation(userId, otherUserId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            log.error("Error getting conversation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDto message) {
        try {
            // Save message to database
            MessageDto savedMessage = messageService.saveMessage(message);

            // Send message to receiver via WebSocket
            messagingTemplate.convertAndSendToUser(
                message.getReceiverId(),
                "/queue/messages",
                savedMessage
            );

            // Send delivery confirmation to sender
            messagingTemplate.convertAndSendToUser(
                message.getSenderId(),
                "/queue/messages",
                savedMessage
            );

            log.info("Message sent from {} to {}", message.getSenderId(), message.getReceiverId());
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    @MessageMapping("/chat.delivered")
    public void markAsDelivered(@Payload Map<String, String> payload) {
        String messageId = payload.get("messageId");
        messageService.markAsDelivered(messageId);
    }

    @MessageMapping("/chat.read")
    public void markAsRead(@Payload Map<String, String> payload) {
        String messageId = payload.get("messageId");
        messageService.markAsRead(messageId);
    }
}


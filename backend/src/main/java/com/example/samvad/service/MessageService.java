package com.example.samvad.service;

import com.example.samvad.dto.MessageDto;
import com.example.samvad.entity.Message;
import com.example.samvad.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageDto saveMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setSenderId(messageDto.getSenderId());
        message.setReceiverId(messageDto.getReceiverId());
        message.setMessageContent(messageDto.getMessageContent());
        message.setTimestamp(System.currentTimeMillis());

        if (messageDto.getMessageType() != null) {
            message.setMessageType(Message.MessageType.valueOf(messageDto.getMessageType()));
        }

        Message savedMessage = messageRepository.save(message);
        log.info("Message saved: {} from {} to {}", savedMessage.getMessageId(),
                 savedMessage.getSenderId(), savedMessage.getReceiverId());

        return convertToDto(savedMessage);
    }

    public List<MessageDto> getConversation(String userId1, String userId2) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(userId1, userId2);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsDelivered(String messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setDelivered(true);
            messageRepository.save(message);
        });
    }

    @Transactional
    public void markAsRead(String messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setRead(true);
            messageRepository.save(message);
        });
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setMessageContent(message.getMessageContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setMessageType(message.getMessageType().name());
        dto.setDelivered(message.isDelivered());
        dto.setRead(message.isRead());
        return dto;
    }
}


package com.example.samvad.repository;

import com.example.samvad.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderId = :userId1 AND m.receiverId = :userId2) OR " +
           "(m.senderId = :userId2 AND m.receiverId = :userId1) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findConversationBetweenUsers(String userId1, String userId2);

    List<Message> findBySenderIdOrReceiverIdOrderByTimestampDesc(String senderId, String receiverId);
}


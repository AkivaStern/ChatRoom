package com.example.ex4.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface for JPA repo, will be the Message repository
 */
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findTop5ByOrderByIdDesc();
    List<Message> findMessagesByMessageContaining(String message);
    List<Message> findMessagesByUsername(String name);
}
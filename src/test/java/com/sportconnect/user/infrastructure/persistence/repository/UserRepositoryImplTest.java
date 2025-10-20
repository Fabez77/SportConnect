package com.sportconnect.user.infrastructure.persistence.repository;


import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("fabio")
                .email("fabio@mail.com")
                .dni("12345678")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save_shouldPersistUser() {
        User saved = userRepository.save(user);
        assertNotNull(saved);
        assertEquals(user.getUsername(), saved.getUsername());
    }

    @Test
    void findById_shouldReturnUser() {
        userRepository.save(user);
        Optional<User> found = userRepository.findById(userId);
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        userRepository.save(user);
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    void delete_shouldRemoveUser() {
        userRepository.save(user);
        userRepository.delete(userId);
        Optional<User> found = userRepository.findById(userId);
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_shouldReturnUser() {
        userRepository.save(user);
        Optional<User> found = userRepository.findByEmail(user.getEmail());
        assertTrue(found.isPresent());
    }

    @Test
    void existsByDni_shouldReturnTrue() {
        userRepository.save(user);
        boolean exists = userRepository.existsByDni(user.getDni());
        assertTrue(exists);
    }
}

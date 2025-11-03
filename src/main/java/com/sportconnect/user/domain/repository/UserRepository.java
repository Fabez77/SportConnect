package com.sportconnect.user.domain.repository;

import com.sportconnect.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface UserRepository {
    Optional<User> findById(UUID id);
    List<User> findAll();
    Page<User> findAll(Map<String, String> filters, String search, Pageable pageable);
    User save(User user);
    void deleteById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByDni(String dni);
}

package com.sportconnect.auth.domain.repository;

import com.sportconnect.auth.domain.model.UserAuth;
import java.util.*;

public interface UserAuthRepository {
    Optional<UserAuth> findById(UUID id);
    List<UserAuth> findAll();
    UserAuth save(UserAuth user);
    void delete(UUID id);
    Optional<UserAuth> findByUsername(String username);
    Optional<UserAuth> findByEmail(String email);
}
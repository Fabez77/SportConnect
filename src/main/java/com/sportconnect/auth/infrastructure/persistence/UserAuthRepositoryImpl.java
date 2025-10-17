package com.sportconnect.auth.infrastructure.persistence;

import com.sportconnect.auth.domain.model.UserAuth;
import com.sportconnect.auth.domain.repository.UserAuthRepository;
import com.sportconnect.auth.infrastructure.mapper.UserAuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserAuthRepositoryImpl implements UserAuthRepository {

    private final UserAuthJpaRepository jpaRepository;
    private final UserAuthMapper mapper;

    @Override
    public Optional<UserAuth> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<UserAuth> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public UserAuth save(UserAuth user) {
        var entity = mapper.toEntity(user);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<UserAuth> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<UserAuth> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }
}
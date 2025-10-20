package com.sportconnect.user.infrastructure.persistence.repository;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           

import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import com.sportconnect.user.infrastructure.mapper.UserEntityMapper;
import com.sportconnect.user.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return mapper.toDomainList(userJpaRepository.findAll());
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userJpaRepository.existsByDni(dni);
    }
}

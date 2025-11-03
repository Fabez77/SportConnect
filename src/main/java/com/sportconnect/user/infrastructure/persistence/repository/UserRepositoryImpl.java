package com.sportconnect.user.infrastructure.persistence.repository;

import com.sportconnect.shared.datatable.filter.SpecificationBuilder;
import com.sportconnect.user.domain.model.User;
import com.sportconnect.user.domain.repository.UserRepository;
import com.sportconnect.user.infrastructure.mapper.UserEntityMapper;
import com.sportconnect.user.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public Page<User> findAll(Map<String, String> filters, String search, Pageable pageable) {
        SpecificationBuilder<UserEntity> builder = new SpecificationBuilder<>();
        Specification<UserEntity> spec = builder.build(filters, search, List.of("username", "email", "dni"));

        Page<UserEntity> page = jpaRepository.findAll(spec, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public boolean existsByDni(String dni) {
        return jpaRepository.existsByDni(dni);
    }
}

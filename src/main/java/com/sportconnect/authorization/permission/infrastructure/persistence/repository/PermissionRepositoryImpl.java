package com.sportconnect.authorization.permission.infrastructure.persistence.repository;

import com.sportconnect.authorization.permission.domain.model.Permission;
import com.sportconnect.authorization.permission.domain.repository.PermissionRepository;
import com.sportconnect.authorization.permission.infrastructure.mapper.PermissionEntityMapper;
import com.sportconnect.authorization.permission.infrastructure.persistence.entity.PermissionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final JpaPermissionRepository jpaRepository;
    private final PermissionEntityMapper mapper;

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = mapper.toEntity(permission);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public Page<Permission> findAll(Specification<PermissionEntity> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Permission> findAllById(List<UUID> ids) {
        return mapper.toDomainList(jpaRepository.findAllById(ids));
    }
}

package com.sportconnect.authorization.role.infrastructure.persistence.repository;

import com.sportconnect.authorization.role.domain.model.Role;
import com.sportconnect.authorization.role.domain.repository.RoleRepository;
import com.sportconnect.authorization.role.infrastructure.mapper.RoleEntityMapper;
import com.sportconnect.authorization.role.infrastructure.persistence.entity.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final JpaRoleRepository jpaRepository;
    private final RoleEntityMapper mapper;

    public RoleRepositoryImpl(JpaRoleRepository jpaRepository, RoleEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = mapper.toEntity(role);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    @Override
    public List<Role> findAllById(List<UUID> ids) {
        List<RoleEntity> entities = jpaRepository.findAllById(ids);
        return mapper.toDomainList(entities);
    }
}

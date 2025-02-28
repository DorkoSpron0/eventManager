package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserDBO, UUID> {
    Optional<UserDBO> findByUsername(String username);
}

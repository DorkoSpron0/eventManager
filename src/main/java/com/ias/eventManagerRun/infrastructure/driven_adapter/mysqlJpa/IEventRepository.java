package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEventRepository extends JpaRepository<EventDBO, UUID> {
}

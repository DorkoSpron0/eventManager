package com.ias.eventManagerRun.infrastructure.repository;

import com.ias.eventManagerRun.domain.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEventRepository extends JpaRepository<Event, UUID> {
}

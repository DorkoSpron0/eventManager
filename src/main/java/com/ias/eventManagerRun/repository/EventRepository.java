package com.ias.eventManagerRun.repository;

import com.ias.eventManagerRun.domain.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}

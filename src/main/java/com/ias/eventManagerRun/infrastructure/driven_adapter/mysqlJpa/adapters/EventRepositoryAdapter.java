package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.infrastructure.mappers.EventMapper;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventRepositoryAdapter implements EventUseCases {

    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;

    public EventRepositoryAdapter(IEventRepository eventRepository, IUserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper.eventDBOToModel)
                .toList();
    }

    @Transactional
    @Override
    public Function<EventModel, EventModel> registerEvent() {
        return (EventModel model) -> {
            boolean founded = eventRepository.existsByName(model.name().value());

            if(founded) throw new DataIntegrityViolationException("Ya existe evento con ese nombre"); // -> si ya existe con ese nombre

            return EventMapper.eventModelToDBO
                    .andThen(eventRepository::save)
                    .andThen(EventMapper.eventDBOToModel)
                    .apply(model);
        };
    }

    @Override
    public Function<UUID, EventModel> getEventById() {
        return (UUID id) -> eventRepository.findById(id)
                .map(EventMapper.eventDBOToModel)
                .orElseThrow(() -> new IllegalArgumentException("Ese evento no fue encontrado"));
    }

    @Transactional
    @Override
    public BiFunction<UUID, EventModel, EventModel> updateEventById() {
        return (UUID id, EventModel model) -> {
            EventDBO current = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ese evento no fue encontrado"));

            Set<UserDBO> usersToSet = Optional.ofNullable(model.userModels()) // Porque puede estar null
                    .filter(users -> !users.isEmpty())// Verifica si está vacía
                    .map(users -> users.stream()// Solo entra al map si pasa el .filter()
                            .map(UserMapper.userModelToDBO)
                            .collect(Collectors.toSet()))
                    .orElse(current.getUserSet()); // <- clave: mantener usuarios actuales si no vienen nuevos

            EventDBO updated = new EventDBO(
                    id,
                    model.name().value(),
                    model.description().value(),
                    model.place(),
                    model.date(),
                    usersToSet
            );

            EventDBO saved = eventRepository.save(updated);
            return EventMapper.eventDBOToModel.apply(saved);
        };
    }

    @Transactional
    @Override
    public BiFunction<UUID, UUID, String> registerUserToEvent() {
        return (UUID userId, UUID eventId) -> {
            UserDBO user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Ese usuario no fue encontrado"));
            EventDBO event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Ese evento no fue encontrado"));

            // Evitar duplicados
            if (event.getUserSet().contains(user)) {
                throw new IllegalArgumentException("Ese usuarios ya está registrado"); // Ya está registrado
            }

            event.getUserSet().add(user);
            eventRepository.save(event);

            return "Usuario registrado al evento correctamente";
        };
    }

    @Override
    public Function<UUID, Set<EventModel>> getAllEventByUserId() {
        return (UUID id) -> userRepository.findById(id) // -> Optional de UserDBO
                .map(user -> user.getEventDBOS()
                        .stream()
                        .map(EventMapper.eventDBOToModel)
                        .collect(Collectors.toSet())
                )
                .orElseThrow(() -> new IllegalArgumentException("Ese usuarios no fue encontrado"));
    }

    @Transactional
    @Override
    public Function<UUID, String> removeEvent() {
        return (UUID userId) -> eventRepository.findById(userId)
                .map(event -> {
                    eventRepository.delete(event);

                    return "El evento fue eliminado exitosamente";
                })
                .orElseThrow(() -> new IllegalArgumentException("Ese evento no fue encontrado"));
    }
}

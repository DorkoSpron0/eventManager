package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.infrastructure.mappers.EventMapper;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class EventRepositoryAdapter implements EventUseCases {

    private IEventRepository eventRepository;
    private IUserRepository userRepository;

    public EventRepositoryAdapter(IEventRepository eventRepository, IUserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Supplier<List<EventModel>> getAllEvents() {
        return () -> eventRepository.findAll().stream()
                .map(EventMapper.eventDBOToModel)
                .toList();
    }

    @Override
    public Function<EventModel, Optional<EventModel>> registerEvent() {
        return (EventModel model) -> {
            boolean founded = eventRepository.existsByName(model.name().value());

            if(founded) return Optional.empty(); // -> si ya existe con ese nombre

            EventDBO dbo = EventMapper.eventModelToDBO.apply(model);
            EventDBO saved = eventRepository.save(dbo);

            return Optional.of(EventMapper.eventDBOToModel.apply(saved));
        };
    }

    @Override
    public Function<UUID, Optional<EventModel>> getEventById() {
        return (UUID id) -> eventRepository.findById(id)
                .map(EventMapper.eventDBOToModel);
    }

    @Override
    public BiFunction<UUID, EventModel, Optional<EventModel>> updateEventById() {
        return (UUID id, EventModel model) -> {
            Optional<EventDBO> maybeEvent = eventRepository.findById(id);

            if (maybeEvent.isEmpty()) return Optional.empty();

            EventDBO current = maybeEvent.get();

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
            return Optional.of(EventMapper.eventDBOToModel.apply(saved));
        };
    }

    @Override
    public BiFunction<UUID, UUID, Optional<String>> registerUserToEvent() {
        return (UUID userId, UUID eventId) -> {
            Optional<UserDBO> userOpt = userRepository.findById(userId);
            Optional<EventDBO> eventOpt = eventRepository.findById(eventId);

            if(userOpt.isEmpty() || eventOpt.isEmpty()){
                return Optional.empty();
            }

            EventDBO event = eventOpt.get();
            UserDBO user = userOpt.get();

            // Evitar duplicados
            if (event.getUserSet().contains(user)) {
                return Optional.empty(); // Ya está registrado
            }

            event.getUserSet().add(user);
            eventRepository.save(event);

            return Optional.of("Usuario registrado al evento correctamente");
        };
    }

    @Override
    public Function<UUID, Optional<Set<EventModel>>> getAllEventByUserId() {
        return (UUID id) -> userRepository.findById(id) // -> Optional de UserDBO
                .map(user -> user.getEventDBOS()
                        .stream()
                        .map(EventMapper.eventDBOToModel)
                        .collect(Collectors.toSet())
                );
    }

    @Override
    public Function<UUID, Optional<String>> removeEvent() {
        return (UUID id) -> eventRepository.findById(id)
                .flatMap(dbo -> {
                    eventRepository.delete(dbo);
                    return Optional.of("Evento eliminado con éxito");
                }
            );
    }
}

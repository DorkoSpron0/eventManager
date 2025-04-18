package com.ias.eventManagerRun;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventDataProvider {

    public static List<EventDBO> getAllEvents(){
        return List.of(
                new EventDBO(UUID.randomUUID(), new EventName("Nombre1"), new EventDescription("Description1"), "San javier", LocalDate.now(), new HashSet<>()),
                new EventDBO(UUID.randomUUID(), new EventName("Nombre2"), new EventDescription("Description1"), "San javier", LocalDate.now(), new HashSet<>()),
                new EventDBO(UUID.randomUUID(), new EventName("Nombre3"), new EventDescription("Description1"), "San javier", LocalDate.now(), new HashSet<>())
        );
    }

    public static EventDBO getEventDBO(){
        return new EventDBO(UUID.randomUUID(), new EventName("event"), new EventDescription("des"), "San Javier", LocalDate.now(), Set.of(
                new UserDBO(UUID.randomUUID(), new Username("nicky"), new Password("password"))
        ));
    }

    public static EventModel getEventModel(){
        return new EventModel(UUID.randomUUID(), new EventName("event"), new EventDescription("des"), LocalDate.now(),"San Javier", Set.of(
                new UserModel(UUID.randomUUID(), new Username("nicky"), new Password("password"))
        ));
    }
}

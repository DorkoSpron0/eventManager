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

public class UserDataProvider {

    public static List<UserDBO> getAllUsers(){
        return List.of(
                new UserDBO(UUID.randomUUID(), new Username("Dorko"), new Password("password"), new HashSet<>()),
                new UserDBO(UUID.randomUUID(), new Username("manu"), new Password("password"), new HashSet<>()),
                new UserDBO(UUID.randomUUID(), new Username("Edison"), new Password("password"), new HashSet<>()),
                new UserDBO(UUID.randomUUID(), new Username("ney"), new Password("password"), new HashSet<>()),
                new UserDBO(UUID.randomUUID(), new Username("poma"), new Password("password"), new HashSet<>()),
                new UserDBO(UUID.randomUUID(), new Username("yolima"), new Password("password"), new HashSet<>())
        );
    }

    public static UserModel getUserModel(){
        return new UserModel(UUID.randomUUID(), new Username("Nicky"), new Password("Password123"), Set.of(
                new EventModel(UUID.randomUUID(),
                        new EventName("Event name"),
                        new EventDescription("Desription"),
                        LocalDate.now(),
                        "San Javier")
        ));
    }

    public static UserDBO getUserDBO(){
        return new UserDBO(UUID.randomUUID(), new Username("Nicky"), new Password("Password123"), Set.of(
                new EventDBO(UUID.randomUUID(),
                                new EventName("Event name"),
                                new EventDescription("Desription"),
                                "San Javier",
                                LocalDate.now(),
                        new HashSet<>()
                )
        ));
    }

    public static String getToken(){
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOaWNreUZGIiwiaWF0IjoxNzQ0ODMyNTUxLCJleHAiOjE3NDQ4MzYxNTF9.RLen7-V-WxczuFDWJRipSkTdRXXSHivqbAvh-g1rfH4";
    }
}

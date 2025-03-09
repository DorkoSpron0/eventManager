package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;

import java.util.List;
import java.util.UUID;

public interface UserUseCases {

    List<UserDBO> getAllUsers();
    UserDBO registerUser(UserDBO user);
    UserDBO findById(UUID id);
    String loginUser(UserDBO user);
    UserDBO findByUsername(String username);
}

package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.User;

import java.util.List;
import java.util.UUID;

public interface UserUseCases {

    List<User> getAllUsers();
    User registerUser(User user);
    User findById(UUID id);
    String loginUser(User user);
    User findByUsername(String username);
}

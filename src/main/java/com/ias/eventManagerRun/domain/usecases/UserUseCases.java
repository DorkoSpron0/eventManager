package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserUseCases {

    List<UserModel> getAllUsers();
    UserModel registerUser(UserModel user);
    UserModel findById(UUID id);
    String loginUser(UserModel user);
    UserModel findByUsername(String username);
}

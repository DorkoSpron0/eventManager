package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.UserModel;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface UserUseCases {

    List<UserModel> getAllUsers();

    Function<UserModel, UserModel> registerUser();

    Function<UUID, UserModel> findById();

    Function<UserModel, String> loginUser();

    Function<String, UserModel> findByUsername();
}

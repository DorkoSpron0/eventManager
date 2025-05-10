package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public interface UserUseCases {

    Supplier<List<UserModel>> getAllUsers();

    Function<UserModel, Optional<UserModel>> registerUser();

    Function<UUID, Optional<UserModel>> findById();

    Function<UserModel, Optional<String>> loginUser();

    Function<String, Optional<UserModel>> findByUsername();
}

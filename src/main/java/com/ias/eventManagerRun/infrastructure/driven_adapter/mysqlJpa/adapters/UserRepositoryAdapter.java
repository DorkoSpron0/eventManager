package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UserRepositoryAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    public UserRepositoryAdapter(JwtService jwtService, PasswordEncoder passwordEncoder, IUserRepository userRepository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Supplier<List<UserModel>> getAllUsers() {
        return () -> userRepository.findAll()
                .stream()
                .map(UserMapper.userDBOToModel)
                .toList();
    }

    @Transactional
    @Override
    public Function<UserModel, Optional<UserModel>> registerUser() {
        return (UserModel user) -> {
            // -> Validar si el usuario existe
            boolean exists = userRepository.existsByUsername(user.username().value());
            if(exists) return Optional.empty();

            // -> Codificar la contraseña
            String encodedPassword = passwordEncoder.encode(user.password().value());

            // -> Se crea un DBO con un nuevo UserModel porque user es inmutable
            UserDBO dbo = UserMapper.userModelToDBO.apply(
                    new UserModel(
                            user.id(),
                            user.username(),
                            new Password(encodedPassword),
                            user.eventModel()
                    )
            );

            UserDBO saved = userRepository.save(dbo);

            // -> Mapear y return
            return Optional.of(UserMapper.userDBOToModel.apply(saved));
        };
    }

    @Override
    public Function<UUID, Optional<UserModel>> findById() {
        return (UUID id) -> userRepository.findById(id)
                .map(UserMapper.userDBOToModel);
    }

    @Override
    public Function<UserModel, Optional<String>> loginUser() {
        // TODO - REFACTOR
        return (UserModel model) -> userRepository.findByUsername(model.username().value()) // -> Esto ya devuelve Optional
                .flatMap(user -> {
                    boolean matches = passwordEncoder.matches(model.password().value(), user.getPassword());
                    if(!matches) return Optional.empty();

                    return Optional.of(jwtService.generateToken(user.getUsername()));
                }
            );
    }

    @Override
    public Function<String, Optional<UserModel>> findByUsername() {
        return (String username) -> userRepository.findByUsername(username)
                .map(UserMapper.userDBOToModel);
    }
}
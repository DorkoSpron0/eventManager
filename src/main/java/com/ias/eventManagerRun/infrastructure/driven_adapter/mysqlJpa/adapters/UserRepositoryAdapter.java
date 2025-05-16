package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UserRepositoryAdapter implements UserUseCases {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryAdapter(JwtService jwtService, PasswordEncoder passwordEncoder, IUserRepository userRepository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper.userDBOToModel)
                .toList();
    }

    @Transactional
    @Override
    public Function<UserModel, UserModel> registerUser() {
        return (UserModel user) -> {
            // -> Validar si el usuario existe
            boolean exists = userRepository.existsByUsername(user.username().value());
            if(exists) throw new DataIntegrityViolationException("Ese username ya existe, fallo al registrarse");

            // -> Codificar la contraseña
            String encodedPassword = passwordEncoder.encode(user.password().value());

            // -> Se crea un DBO con un nuevo UserModel porque user es inmutable
            return UserMapper.userModelToDBO
                    .andThen(dbo -> userRepository.save(dbo))
                    .andThen(UserMapper.userDBOToModel)
                    .apply(new UserModel( // -> Es el que se va a mapear en userModelToDBO
                            user.id(),
                            user.username(),
                            new Password(encodedPassword),
                            user.eventModel()
                    )
                );
        };
    }

    @Override
    public Function<UUID, UserModel> findById() {
        return (UUID id) -> userRepository.findById(id)
                .map(UserMapper.userDBOToModel)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario"));
    }

    @Override
    public Function<UserModel, String> loginUser() {
        return (UserModel model) -> userRepository.findByUsername(model.username().value())
                .map(user -> {
                    if(!passwordEncoder.matches(model.password().value(), user.getPassword())){
                        throw new IllegalArgumentException("Las contraseñas no coinciden");
                    }

                    return jwtService.generateToken(model.username().value());
                })
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario"));
    }

    @Override
    public Function<String, UserModel> findByUsername() {
        return (String username) -> userRepository.findByUsername(username)
                .map(UserMapper.userDBOToModel)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario"));
    }
}
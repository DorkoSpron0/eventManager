package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IUserRepositoryAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    private final Function<UserModel, UserModel> hashPassword = (UserModel model) -> {
        String rawPassword = model.getPassword().getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        model.setPassword(new Password(encodedPassword));
        return model;
    };

    private final Function<UserModel, UserModel> saveUser =
            hashPassword // primero la cifra
                    .andThen(UserMapper.functionUserModelToDBO)
                    .andThen(userDBO -> userRepository.save(userDBO))
                    .andThen(UserMapper.functionUserDBOToModel);

    // FIND BY ID
    private final Function<UUID, UserDBO> findUserDBOById =
            id -> userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // FIND BY USERNAME
    private final Function<String, UserDBO> findUserDBOByUsername =
            (String username) -> userRepository.findByUsername_Username(username).orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found"));

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper.functionUserDBOToModel)
                .toList();
    }

    @Transactional
    @Override
    public UserModel registerUser(UserModel user) {
        return saveUser.apply(user);
    }

    @Override
    public UserModel findById(UUID id) {
        return UserMapper.functionUserDBOToModel.apply(findUserDBOById.apply(id));
    }

    @Override
    public String loginUser(UserModel userModel) {
        UserDBO userDBOFounded = findUserDBOByUsername.apply(userModel.getUsername().getUsername());

        if(passwordEncoder.matches(userModel.getPassword().getPassword(), userDBOFounded.getPassword().getPassword())){
            return jwtService.generateToken(userDBOFounded.getUsername().getUsername());
        }

        throw new IllegalArgumentException("Invalid credentials"); // No especificar cual falló
    }

    @Override
    public UserModel findByUsername(String username) {
        return UserMapper.functionUserDBOToModel.apply(findUserDBOByUsername.apply(username));
    }
}
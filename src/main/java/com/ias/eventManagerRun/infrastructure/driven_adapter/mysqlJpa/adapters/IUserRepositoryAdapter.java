package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IUserRepositoryAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtService jwtService;

    private final Function<UserModel, UserModel> saveUser =
            UserMapper.functionUserModelToDBO
                    .andThen(userDBO -> userRepository.save(userDBO))
                    .andThen(userDBO -> UserMapper.functionUserDBOToModel.apply(userDBO));

    // FIND BY ID
    private final Function<UUID, UserDBO> findUserDBOById =
            id -> userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
    private final Function<UUID, UserModel> findUserModelById =
            findUserDBOById.andThen(UserMapper.functionUserDBOToModel);

    // FIND BY USERNAME
    private final Function<String, UserModel> findUserById =
            (String username) -> UserMapper.functionUserDBOToModel.apply(
                    userRepository.findByUsername_Username(username).orElseThrow(() -> new UsernameNotFoundException("Not found"))
            );

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper.functionUserDBOToModel)
                .toList();
    }

    @Override
    public UserModel registerUser(UserModel user) {
        return saveUser.apply(user);
    }

    @Override
    public UserModel findById(UUID id) {
        return findUserModelById.apply(id);
    }

    @Override
    public String loginUser(UserModel userDBO) {
        UserDBO userDBOFounded = UserMapper.userModelToDBO(findByUsername(userDBO.getUsername().getUsername()));

        if(userDBO.getPassword().equals(userDBOFounded.getPassword())){
            return jwtService.generateToken(userDBOFounded.getUsername().getUsername());
        }

        throw  new IllegalArgumentException("Password not match");
    }

    @Override
    public UserModel findByUsername(String username) {
        return findUserById.apply(username);
    }
}
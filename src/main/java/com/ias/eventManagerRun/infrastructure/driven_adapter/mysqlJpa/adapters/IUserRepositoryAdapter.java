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

@Service
@AllArgsConstructor
public class IUserRepositoryAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtService jwtService;


    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::userDBOToModel).toList();
    }

    @Override
    public UserModel registerUser(UserModel user) {
        return UserMapper.userDBOToModel(userRepository.save(UserMapper.userModelToDBO(user)));
    }

    @Override
    public UserModel findById(UUID id) {
        return UserMapper.userDBOToModel(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
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
        return UserMapper.userDBOToModel(userRepository.findByUsername_Username(username).orElseThrow(() -> new UsernameNotFoundException("Not found")));
    }
}

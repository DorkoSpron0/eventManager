package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.app.config.JwtService;
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
        return userRepository.findAll().stream().map(UserDBO::toDomain).toList();
    }

    @Override
    public UserModel registerUser(UserModel userModel) {
        UserDBO user = UserDBO.fromDomain(userModel);

        System.out.println(user.toString());

        return userRepository.save(user).toDomain();
    }

    @Override
    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")).toDomain();
    }

    @Override
    public String loginUser(UserModel userDBO) {
        UserModel userDBOFounded = findByUsername(userDBO.getUsername());

        if(userDBO.getPassword().equals(userDBOFounded.getPassword())){
            return jwtService.generateToken(userDBOFounded.getUsername());
        }

        throw  new IllegalArgumentException("Password not match");
    }

    @Override
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found")).toDomain();
    }
}

package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

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
    public List<UserDBO> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDBO registerUser(UserDBO user) {
        return userRepository.save(user);
    }

    @Override
    public UserDBO findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public String loginUser(UserDBO userDBO) {
        UserDBO userDBOFounded = findByUsername(userDBO.getUsername().getUsername());

        if(userDBO.getPassword().equals(userDBOFounded.getPassword())){
            return jwtService.generateToken(userDBOFounded.getUsername().getUsername());
        }

        throw  new IllegalArgumentException("Password not match");
    }

    @Override
    public UserDBO findByUsername(String username) {
        return userRepository.findByUsername_Username(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }
}

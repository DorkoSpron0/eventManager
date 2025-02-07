package com.ias.eventManagerRun.services;

import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserUseCases {

    private UserRepository userRepository;
    private JwtService jwtService;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public String loginUser(User user) {
        User userFounded = findByUsername(user.getUsername());

        if(user.getPassword().equals(userFounded.getPassword())){
            return jwtService.generateToken(userFounded.getUsername());
        }

        throw  new IllegalArgumentException("Password not match");
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }
}

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

@Service
@AllArgsConstructor
public class IUserRepositoryAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::userDBOToModel)
                .toList();
    }

    @Transactional // Si algo falla hace rollback y no persiste los cambios
    @Override
    public UserModel registerUser(UserModel user) {
        user.setPassword(new Password(passwordEncoder.encode(user.getPassword().getPassword())));

        UserDBO userDBO = UserMapper.userModelToDBO(user);
        return UserMapper.userDBOToModel(userRepository.save(userDBO));
    }

    @Override
    public UserModel findById(UUID id) {
        return UserMapper.userDBOToModel(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @Override
    public String loginUser(UserModel userModel) {
        UserDBO userDBOFounded = userRepository.findByUsername_Username(userModel.getUsername().getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username '" + userModel.getUsername().getUsername() + "' not found"));

        // TODO - REFACTOR TO ANOTHER FUNCTION
        if(passwordEncoder.matches(userModel.getPassword().getPassword(), userDBOFounded.getPassword().getPassword())){
            return jwtService.generateToken(userDBOFounded.getUsername().getUsername());
        }

        throw new IllegalArgumentException("Invalid credentials"); // No especificar cual falló
    }

    @Override
    public UserModel findByUsername(String username) {
        return UserMapper.userDBOToModel(userRepository.findByUsername_Username(username).orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found")));
    }
}
package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.UserDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.response.UserResponseInfo;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.ias.eventManagerRun.infrastructure.mappers.UserMapper.userDTORequireToModel;

@RestController
@RequestMapping("/iasapi/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(this.userUseCases.getAllUsers()
                .get().stream()
                .map(UserMapper.userModelToResponseDTO)
        ); // Map to DTO
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){

        UserModel model = UserMapper.userDTORequireToModel.apply(user);

        Optional<UserModel> userOpt = this.userUseCases.registerUser().apply(model);

        if(userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciales incorrectas");
        }

        UserModel saved = userOpt.get();
        UserResponseInfo response = UserMapper.userModelToResponseDTO.apply(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

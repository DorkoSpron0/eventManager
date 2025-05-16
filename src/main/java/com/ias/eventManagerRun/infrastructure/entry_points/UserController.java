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


@RestController
@RequestMapping("/iasapi/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(this.userUseCases.getAllUsers()
                .stream()
                .map(UserMapper.userModelToResponseDTO)
        ); // Map to DTO
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){

        UserModel model = UserMapper.userDTORequireToModel.apply(user);

        UserModel userOpt = this.userUseCases.registerUser().apply(model);

        UserResponseInfo response = UserMapper.userModelToResponseDTO.apply(userOpt);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

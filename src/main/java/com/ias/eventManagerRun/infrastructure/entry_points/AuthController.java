package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.UserDTO;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iasapi/auth")
@AllArgsConstructor
public class AuthController {

    private UserUseCases userUseCases;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO user){
        UserModel model = UserMapper.userDTORequireToModel.apply(user);
        String tokenOpt = this.userUseCases.loginUser().apply(model);

        return ResponseEntity.status(HttpStatus.OK).body(tokenOpt);
    }
}

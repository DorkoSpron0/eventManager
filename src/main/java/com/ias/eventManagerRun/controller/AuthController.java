package com.ias.eventManagerRun.controller;

import com.ias.eventManagerRun.controller.DTO.UserDTO;
import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iasapi/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(user.toDomain()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

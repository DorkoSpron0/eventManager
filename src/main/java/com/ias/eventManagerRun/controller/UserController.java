package com.ias.eventManagerRun.controller;

import com.ias.eventManagerRun.controller.DTO.UserDTO;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
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
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userUseCases.getAllUsers());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userUseCases.registerUser(user.toDomain()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

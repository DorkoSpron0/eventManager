package com.ias.eventManagerRun.controller;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iasapi/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping
    public List<User> getAllUser(){
        return userUseCases.getAllUsers();
    }

    @PostMapping
    public User registerUser(@RequestBody User user){
        return userUseCases.registerUser(user);
    }

}

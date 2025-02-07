package com.ias.eventManagerRun.controller;

import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iasapi/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/login")
    public String loginUser(@RequestBody User user){
        return userService.loginUser(user);
    }
}

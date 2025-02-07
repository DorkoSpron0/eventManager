package com.ias.eventManagerRun.controller;

import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.services.UserService;
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
    public ResponseEntity<?> loginUser(@RequestBody User user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(user));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

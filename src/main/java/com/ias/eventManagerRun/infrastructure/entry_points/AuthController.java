package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IUserRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
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

    private IUserRepositoryAdapter userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDBO user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(UserMapper.userDBOToModel(user)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

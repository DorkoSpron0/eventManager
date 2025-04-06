package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iasapi/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        try{
            //List<UserDBO> userList = userUseCases.getAllUsers();
            List<UserDTO> dtos = userUseCases.getAllUsers().stream().map(UserMapper.functionUserModelToDTO).toList();

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            UserDBO userDBO = UserMapper.functionDTOToDBO.apply(user);

            UserDBO dbo = UserMapper.functionUserModelToDBO.apply(userUseCases.registerUser(UserMapper.functionUserDBOToModel.apply(userDBO)));

            UserDTO dto = UserMapper.functionDBOToDTO.apply(dbo);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

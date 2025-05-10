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

    private final UserUseCases userUseCases;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        try{
            //List<UserDBO> userList = userUseCases.getAllUsers();
            List<UserDTO> dtos = userUseCases.getAllUsers().stream().map(UserMapper::userModelToDTO).toList();

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            UserDBO userDBO = UserMapper.userDTOTODBO(user);

            UserDBO dbo = UserMapper.userModelToDBO(userUseCases.registerUser(UserMapper.userDBOToModel(userDBO)));

            UserDTO dto = UserMapper.userDBoToDTO(dbo);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

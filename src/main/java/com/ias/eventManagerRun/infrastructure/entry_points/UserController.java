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

            List<UserDTO> dtos = UserMapper.listUserDToFromUserUseCasesWithEventsWithoutUser(userUseCases);

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            UserDBO userDBO = UserMapper.userDTOToDBO(user);

            UserDBO dbo = userUseCases.registerUser(userDBO);

            UserDTO dto = UserMapper.userDBOToDTO(dbo);


            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

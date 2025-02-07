package com.ias.eventManagerRun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ias.eventManagerRun.controller.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerUserSuccessFully() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .username("Nicky")
                .password("password123")
                .build();

        this.mockMvc.perform(post("/iasapi/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerUserWithBadData() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .username("").build();
        this.mockMvc.perform(post("/iasapi/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getAllUsersWithoutToken() throws Exception{
        this.mockMvc.perform(get("/iasapi/users")
                .header("Authorization", "")
        ).andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsersSuccessfully() throws Exception {
        this.mockMvc.perform(get("/iasapi/users")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNYW51ZWxhIiwiaWF0IjoxNzM4OTY1Nzk4LCJleHAiOjE3Mzg5NjkzOTh9.AWLlVgBAN-SDb8LkOW1B-WOt7ViLNftAevcGWy3Yv98")
        ).andExpect(status().isOk());
    }
}

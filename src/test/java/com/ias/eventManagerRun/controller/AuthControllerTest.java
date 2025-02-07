package com.ias.eventManagerRun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ias.eventManagerRun.controller.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginUserSuccessfully() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .username("Manuela")
                .password("password123")
                .build();
        this.mockMvc.perform(post("/iasapi/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNYW51ZWxhIiwiaWF0IjoxNzM4OTgxNDU1LCJleHAiOjE3Mzg5ODUwNTV9.fIMhRFEcniBvdmjLGHRG6hsKj_XK2-K995fRI5HZsvo")
        ).andExpect(status().isOk());
    }
}

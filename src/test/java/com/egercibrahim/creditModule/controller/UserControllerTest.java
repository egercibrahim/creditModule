package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.enums.Role;
import com.egercibrahim.creditModule.model.User;
import com.egercibrahim.creditModule.record.CreateUserRequest;
import com.egercibrahim.creditModule.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_customer_by_admin_success() throws Exception {
        CreateUserRequest request = new CreateUserRequest("cust1", "admin", "CUSTOMER");
        when(passwordEncoder.encode("admin")).thenReturn("encoded-password");

        when(userService.save(
                User.builder()
                        .username("cust1")
                        .password("encoded-password")
                        .role(Role.CUSTOMER)
                        .build()))
                .thenReturn(User.builder()
                        .username("cust1")
                        .role(Role.CUSTOMER)
                        .build());

        mockMvc.perform(post("/api/v1/credit/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("cust1"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_admin_by_admin_success() throws Exception {
        CreateUserRequest request = new CreateUserRequest("admin1", "admin", "ADMIN");
        when(passwordEncoder.encode("admin")).thenReturn("encoded-password");

        when(userService.save(any(User.class)))
                .thenReturn(User.builder()
                        .id(1L)
                        .username("admin1")
                        .role(Role.ADMIN)
                        .build());

        mockMvc.perform(post("/api/v1/credit/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin1"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void create_customer_by_customer_fail() throws Exception {
        CreateUserRequest request = new CreateUserRequest("cust1", "admin", "CUSTOMER");

        mockMvc.perform(post("/api/v1/credit/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void create_admin_by_customer_fail() throws Exception {
        CreateUserRequest request = new CreateUserRequest("cust1", "admin", "ADMIN");

        mockMvc.perform(post("/api/v1/credit/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.enums.Role;
import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.User;
import com.egercibrahim.creditModule.record.CreateCustomerRequest;
import com.egercibrahim.creditModule.service.CustomerService;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_customer_by_admin_success() throws Exception {
        User user = User.builder()
                .id(2L)
                .username("cust1")
                .password("admin")
                .role(Role.CUSTOMER)
                .build();

        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        CreateCustomerRequest request = new CreateCustomerRequest(
                user.getUsername(),
                "John",
                "Doe",
                new BigDecimal("1000000")
        );

        Customer customer = Customer.builder()
                .id(1L)
                .user(user)
                .name(request.name())
                .surname(request.surname())
                .creditLimit(request.creditLimit())
                .usedCreditLimit(BigDecimal.ZERO)
                .build();

        when(customerService.save(anyString(), anyString(), anyString(), any(BigDecimal.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/credit/customer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.surname").value(request.surname()))
                .andExpect(jsonPath("$.creditLimit").value(request.creditLimit().intValue()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_admin_by_admin_success() throws Exception {
        User user = User.builder()
                .id(3L)
                .username("admin1")
                .password("adminpass")
                .role(Role.ADMIN)
                .build();

        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        CreateCustomerRequest request = new CreateCustomerRequest(
                user.getUsername(),
                "AdminName",
                "AdminSurname",
                new BigDecimal("1000000")
        );

        Customer customer = Customer.builder()
                .id(1L)
                .user(user)
                .name(request.name())
                .surname(request.surname())
                .creditLimit(request.creditLimit())
                .usedCreditLimit(BigDecimal.ZERO)
                .build();

        when(customerService.save(anyString(), anyString(), anyString(), any(BigDecimal.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/credit/customer")
                        .with(csrf())  // ✅ CSRF token for POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.surname").value(request.surname()))
                .andExpect(jsonPath("$.creditLimit").value(request.creditLimit().intValue())); // BigDecimal to int
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void create_customer_by_customer_fail() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "cust1",
                "John",
                "Doe",
                new BigDecimal("10000")
        );

        mockMvc.perform(post("/api/v1/credit/customer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // ✅ Expect 403
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void create_customerAdmin_by_customer_fail() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "admin1",
                "Admin",
                "Surname",
                new BigDecimal("20000")
        );

        mockMvc.perform(post("/api/v1/credit/customer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // ✅ Expect 403
    }


}

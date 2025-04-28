package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.record.CreateLoanRequest;
import com.egercibrahim.creditModule.service.CustomerService;
import com.egercibrahim.creditModule.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_loan_by_admin_success() throws Exception {
        CreateLoanRequest request = new CreateLoanRequest(
                1L,
                new BigDecimal("5000"),
                new BigDecimal("0.2"),
                12
        );

        Loan loan = Loan.builder()
                .id(1L)
                .loanAmount(request.amount().multiply(BigDecimal.valueOf(1.2)))
                .numberOfInstallments(12)
                .createDate(LocalDate.now())
                .isPaid(false)
                .customer(Customer.builder().id(1L).build())
                .build();

        when(loanService.createLoan(
                request.customerId(),
                request.amount(),
                request.interestRate(),
                request.numberOfInstallments()
        )).thenReturn(loan);

        mockMvc.perform(post("/api/v1/credit/loan")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanAmount").value(6000.0))
                .andExpect(jsonPath("$.numberOfInstallments").value(12));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void create_loan_by_customer_fail() throws Exception {
        CreateLoanRequest request = new CreateLoanRequest(
                1L,
                new BigDecimal("5000"),
                new BigDecimal("0.2"),
                12
        );

        mockMvc.perform(post("/api/v1/credit/loan")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void get_loan_by_admin_success() throws Exception {
        Loan loan = Loan.builder()
                .id(1L)
                .loanAmount(new BigDecimal("6000"))
                .numberOfInstallments(12)
                .isPaid(false)
                .createDate(LocalDate.now())
                .customer(Customer.builder().id(1L).build())
                .build();

        when(loanService.getLoansByCustomer(any(), any(), any(), any())).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/v1/credit/loan")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanAmount").value(6000.0))
                .andExpect(jsonPath("$[0].numberOfInstallments").value(12));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void get_loan_by_customer_success() throws Exception {
        Loan loan = Loan.builder()
                .id(1L)
                .loanAmount(new BigDecimal("3000"))
                .numberOfInstallments(6)
                .isPaid(false)
                .createDate(LocalDate.now())
                .customer(Customer.builder().id(1L).build())
                .build();

        when(loanService.getLoansByCustomer(any(), any(), any(), any())).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/v1/credit/loan")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanAmount").value(3000.0))
                .andExpect(jsonPath("$[0].numberOfInstallments").value(6));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void get_loan_by_customer_fail() throws Exception {
        when(loanService.getLoansByCustomer(any(), any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/credit/loan")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

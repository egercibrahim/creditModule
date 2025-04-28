package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.model.LoanInstallment;
import com.egercibrahim.creditModule.record.PaymentRequest;
import com.egercibrahim.creditModule.service.LoanInstallmentService;
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

@WebMvcTest(LoanInstallmentController.class)
class LoanInstallmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanInstallmentService loanInstallmentService;

    @MockitoBean
    private LoanService loanService;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void pay_installment_by_admin_success() throws Exception {
        com.egercibrahim.creditModule.record.PaymentResult result
                = new com.egercibrahim.creditModule.record.PaymentResult(
                2,
                new BigDecimal("2000"),
                true);

        when(loanInstallmentService.payLoan(any(), any(), any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/credit/installment")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentRequest(1L, new BigDecimal("2000")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfInstallmentsPaid").value(2))
                .andExpect(jsonPath("$.totalAmountSpent").value(2000.0))
                .andExpect(jsonPath("$.isLoanFullyPaid").value(true));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void pay_installment_by_customer_fail() throws Exception {
        when(loanInstallmentService.payLoan(any(), any(), any()))
                .thenThrow(new RuntimeException("Unauthorized access"));

        mockMvc.perform(post("/api/v1/credit/installment")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentRequest(1L, new BigDecimal("2000")))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void get_installment_by_admin_success() throws Exception {
        LoanInstallment installment = LoanInstallment.builder()
                .id(1L)
                .loan(Loan.builder().id(1L).build())
                .amount(new BigDecimal("1000"))
                .dueDate(LocalDate.now().plusMonths(1))
                .isPaid(false)
                .build();

        when(loanInstallmentService.getInstallmentsByLoan(any(), any()))
                .thenReturn(List.of(installment));

        mockMvc.perform(get("/api/v1/credit/installment?loanId=1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0))
                .andExpect(jsonPath("$[0].isPaid").value(false));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void get_installment_by_customer_success() throws Exception {
        LoanInstallment installment = LoanInstallment.builder()
                .id(1L)
                .loan(Loan.builder().id(1L).build())
                .amount(new BigDecimal("500"))
                .dueDate(LocalDate.now().plusMonths(1))
                .isPaid(false)
                .build();

        when(loanInstallmentService.getInstallmentsByLoan(any(), any()))
                .thenReturn(List.of(installment));

        mockMvc.perform(get("/api/v1/credit/installment?loanId=1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(500.0))
                .andExpect(jsonPath("$[0].isPaid").value(false));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void get_installment_by_customer_fail() throws Exception {
        when(loanInstallmentService.getInstallmentsByLoan(any(), any()))
                .thenThrow(new RuntimeException("Unauthorized access"));

        mockMvc.perform(get("/api/v1/credit/installment?loanId=99")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

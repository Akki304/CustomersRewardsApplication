package com.retailer.rewardcalculator.controller;

import com.retailer.rewardcalculator.dto.CustomerTransactionDetailsDTO;
import com.retailer.rewardcalculator.exception.CustomerNotFoundException;
import com.retailer.rewardcalculator.exception.InvalidDateRangeException;
import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.service.CustomerRewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerRewardControllerTest
{
    @Mock
    private CustomerRewardService customerRewardService;

    @InjectMocks
    private CustomerRewardContoller customerRewardContoller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Necessary for RequestContextHolder.setRequestAttributes
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void createCustomer() {

        CustomerDetails customer = new CustomerDetails(); // Create a mock CustomerDetails object

        ResponseEntity<?> responseEntity = customerRewardContoller.createCustomer(customer);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User successfully created", responseEntity.getBody());
        verify(customerRewardService, times(1)).saveCustomerDetails(customer);
    }

    @Test
    public void rewardCalculator() throws CustomerNotFoundException, InvalidDateRangeException {

        int customerId = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);
        CustomerTransactionDetailsDTO dto = new CustomerTransactionDetailsDTO(); // Mock DTO
        when(customerRewardService.rewardCalculator(customerId, startDate, endDate)).thenReturn(dto);

        ResponseEntity<?> responseEntity = customerRewardContoller.getRewardPoints(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dto, responseEntity.getBody());
        verify(customerRewardService, times(1)).rewardCalculator(customerId, startDate, endDate);
    }

    @Test
    public void rewardCalculator_RuntimeException() throws CustomerNotFoundException, InvalidDateRangeException {

        int customerId = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);
        String errorMessage = "Simulated error";
        when(customerRewardService.rewardCalculator(customerId, startDate, endDate)).thenThrow(new RuntimeException(errorMessage));


        ResponseEntity<?> responseEntity = customerRewardContoller.getRewardPoints(customerId, startDate, endDate);


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
        verify(customerRewardService, times(1)).rewardCalculator(customerId, startDate, endDate);
    }

}

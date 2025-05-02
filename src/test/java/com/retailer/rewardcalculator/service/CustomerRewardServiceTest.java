package com.retailer.rewardcalculator.service;

import com.retailer.rewardcalculator.dto.CustomerTransactionDetailsDTO;
import com.retailer.rewardcalculator.exception.CustomerNotFoundException;
import com.retailer.rewardcalculator.exception.InvalidDateRangeException;
import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.model.TransactionDetails;
import com.retailer.rewardcalculator.repository.CustomerDetailsRepository;
import com.retailer.rewardcalculator.repository.TransactionDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerRewardServiceTest
{
    @Mock
    private CustomerDetailsRepository customerRepository;
    @Mock
    private TransactionDetailsRepository transactionRepository;
    @InjectMocks
    private CustomerRewardService rewardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /**
     * Test case to verify the data is being saved
     */
    @Test
    public void saveCustomerDetails_withTransactions() {
        // Arrange
        List<TransactionDetails> transactions = Arrays.asList(
                createTransactionDetails(1, 75, LocalDate.now()),
                createTransactionDetails(2, 120, LocalDate.now())
        );
        CustomerDetails customer = createCustomerDetails(1, "abc", transactions);

        rewardService.saveCustomerDetails(customer);

        // Verify that the customerRepository.save method was called once with the customer object
        verify(customerRepository, times(1)).save(customer);
    }

    /**
     * Test case to Calculate the reward points for a specifc customer
     * @throws CustomerNotFoundException
     * @throws InvalidDateRangeException
     */
    @Test
    public void rewardCalculator_customerFound_withTransactions() throws CustomerNotFoundException, InvalidDateRangeException {

        int customerId = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);

        List<TransactionDetails> transactions = Arrays.asList(
                createTransactionDetails(1, 80, LocalDate.of(2024, 1, 15)),
                createTransactionDetails(2, 150, LocalDate.of(2024, 2, 20)),
                createTransactionDetails(3, 60, LocalDate.of(2024, 3, 10))
        );
        CustomerDetails customer = createCustomerDetails(customerId, "Test User", transactions);

        when(customerRepository.findByCustomerId(customerId)).thenReturn(customer); // Changed to findByCustomerId
        when(transactionRepository.findByCustomerAndTransactionDateBetween(customer, startDate, endDate))
                .thenReturn(transactions);


        CustomerTransactionDetailsDTO dto = rewardService.rewardCalculator(customerId, startDate, endDate);

        assertEquals(customerId, dto.getCustomerId());
        assertEquals("Test User", dto.getName());
        assertEquals(transactions.size(), dto.getTransaction().size());
        assertEquals(190, dto.getTotalPoints());

        Map<String, Integer> expectedMonthlyPoints = new HashMap<>();
        expectedMonthlyPoints.put("Jan", 30);
        expectedMonthlyPoints.put("Feb", 150);
        expectedMonthlyPoints.put("Mar", 10);
        assertEquals(expectedMonthlyPoints, dto.getMonthlyPoints());
    }

    /**
     * Test case to throw an exception when Customer id not found in db
     */
    @Test
    public void rewardCalculator_customerNotFound_shouldThrowException() {

        int customerId = 1;
        when(customerRepository.findByCustomerId(customerId)).thenReturn(null);

        assertThrows(CustomerNotFoundException.class, () -> {
            rewardService.rewardCalculator(customerId, null, null);
        });
    }

    /**
     * Test case to throw an exception when End date is before start date
     */
    @Test
    public void rewardCalculator_invalidDateRange_shouldThrowException() {
        int customerId = 1;
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        CustomerDetails customer = createCustomerDetails(customerId, "Test User", new ArrayList<>());
        when(customerRepository.findByCustomerId(customerId)).thenReturn(customer);


        assertThrows(InvalidDateRangeException.class, () -> {
            rewardService.rewardCalculator(customerId, startDate, endDate);
        });
    }


    // Helper method to create a createCustomerDetails object
    private CustomerDetails createCustomerDetails(int customerId, String name, List<TransactionDetails> transactions) {
        CustomerDetails customer = new CustomerDetails();
        customer.setCustomerId(customerId);
        customer.setName(name);
        customer.setTransactions(transactions);
        return customer;
    }

    // Helper method to create a TransactionDetails object
    private TransactionDetails createTransactionDetails(int transactionId, int amount, LocalDate date) {
        TransactionDetails transaction = new TransactionDetails();
        transaction.setTransactionId(transactionId);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(date);
        return transaction;
    }

}

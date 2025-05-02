package com.retailer.rewardcalculator.service;

import com.retailer.rewardcalculator.dto.CustomerTransactionDetailsDTO;
import com.retailer.rewardcalculator.dto.TransactionDTO;
import com.retailer.rewardcalculator.exception.CustomerNotFoundException;
import com.retailer.rewardcalculator.exception.InvalidDateRangeException;
import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.model.TransactionDetails;
import com.retailer.rewardcalculator.repository.CustomerDetailsRepository;
import com.retailer.rewardcalculator.repository.TransactionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerRewardService {

    private final CustomerDetailsRepository customerRepository;
    private final TransactionDetailsRepository transactionRepository;

    @Autowired
    public CustomerRewardService(CustomerDetailsRepository customerRepository, TransactionDetailsRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     *
     * @param customerDetails
     */
    public void saveCustomerDetails(CustomerDetails customerDetails)
    {
        if (customerDetails.getTransactions() != null) {
            for (TransactionDetails transaction : customerDetails.getTransactions())
            {
                transaction.setCustomer(customerDetails);
            }
        }
        customerRepository.save(customerDetails);

    }

    /**
     *
     * @param customerId The unique identifier of the customer to retrieve
     * @param startDate The starting date
     * @param endDate The ending date
     * @return CustomerTransactionDetailsDTO which contains the customerDetails ,
     * transaction details of that customer,
     * monthly reward points,
     * total reward points,
     * @throws CustomerNotFoundException
     */
    public CustomerTransactionDetailsDTO rewardCalculator(int customerId, LocalDate startDate, LocalDate endDate) throws CustomerNotFoundException, InvalidDateRangeException {


        // 1. Fetch Customer
        Optional<CustomerDetails> customerOptional = Optional.ofNullable(customerRepository.findByCustomerId(customerId)); // Use findById with the correct ID type
        CustomerDetails customerDetails = customerOptional.orElseThrow(() -> new CustomerNotFoundException("Customer with given Id is not present"));


        List<TransactionDetails> transactionDetails;
        if (startDate == null && endDate == null)
        {

            transactionDetails= transactionRepository.findByCustomer(customerDetails);
        }
         else
         {
             //check the date
             if(startDate!=null && endDate==null)
             {
                 transactionDetails = transactionRepository.findByCustomerAndTransactionDateGreaterThanEqual(customerDetails, startDate);

             }
             else if(endDate!=null&& startDate==null)
             {
                 transactionDetails = transactionRepository.findByCustomerAndTransactionDateLessThanEqual(customerDetails, endDate);

             }
             else if(startDate!=null&& endDate!=null&&!(endDate.isBefore(startDate)))
             {
                 transactionDetails = transactionRepository.findByCustomerAndTransactionDateBetween(customerDetails, startDate, endDate);

             }
             else
             {
                 throw new InvalidDateRangeException("End date cannot be before start date.");
             }
         }


        CustomerTransactionDetailsDTO customerTransactionDTO = new CustomerTransactionDetailsDTO();
        customerTransactionDTO.setCustomerId(customerId);
        customerTransactionDTO.setName(customerDetails.getName());

        List<TransactionDTO> transactionDTO = convertToTransactionDTOs(transactionDetails);
        customerTransactionDTO.setTransaction(transactionDTO);
        customerTransactionDTO.setMonthlyPoints(monthlyPointsCalculator(transactionDetails));
        customerTransactionDTO.setTotalPoints(calculateTotalRewardPoints(transactionDetails));

        return customerTransactionDTO;
    }

    /**
     *
     * @param transactionDetails List of transaction details
     * @return List of TransactionDTO
     */
    private List<TransactionDTO> convertToTransactionDTOs(List<TransactionDetails> transactionDetails)
    {
        return transactionDetails.stream().map(
                txnDetails -> {
                    TransactionDTO txnDTO = new TransactionDTO();
                    txnDTO.setTransactionId(txnDetails.getTransactionId());
                    txnDTO.setTransactionAmount(txnDetails.getTransactionAmount());
                    txnDTO.setTransactionDate(txnDetails.getTransactionDate());
                    return txnDTO;
                }).collect(Collectors.toList());

    }

    /**
     *
     * @param transactionDetails List of transaction details
     * @return total points earned by the customer
     */
    public int calculateTotalRewardPoints(List<TransactionDetails> transactionDetails)
    {
        int totalPoints = 0;
        for (TransactionDetails transaction : transactionDetails) {
            totalPoints += calculateRewardPoints(transaction.getTransactionAmount());
        }
        return totalPoints;
    }

    /**
     *
     * @param transactionDetails list of transactionDetails
     * @return A map where the keys are strings representing the year and month (e.g., "2025-05")
     * and the values are the total points earned by the customer in that month.
     * Returns an empty map if the input list is empty or null.
     */
    private Map<String, Integer> monthlyPointsCalculator(List<TransactionDetails> transactionDetails)
    {
        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (TransactionDetails transaction : transactionDetails) {
            int points = calculateRewardPoints(transaction.getTransactionAmount());
            String month = transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("MMM", java.util.Locale.US)); //transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("MMM", java.util.Locale.US));
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
        }
        return monthlyPoints;

    }

    /**
     *
     * @param transactionAmount transaction amount for a particular transaction
     * @return total points earned by the customer for a particulat transaction amount
     */
    private int calculateRewardPoints(int transactionAmount)
    {
        if (transactionAmount > 100)
        {
            return 2 * (transactionAmount - 100) + 50; // 2 points for every dollar spent over $100 + 1 point for every dollar between $50 and $100
        } else if (transactionAmount > 50)
        {
            return transactionAmount - 50; // 1 point for every dollar spent between $50 and $100
        }
            return 0;

    }

}

package com.retailer.rewardcalculator.repository;

import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {
    /**
     * Finds all transaction details associated with a specific customer within a given date range (inclusive).
     *
     * @param customer The {@link CustomerDetails} object representing the customer.
     * @param startDate The starting date of the range (inclusive).
     * @param endDate   The ending date of the range (inclusive).
     * @return A list of {@link TransactionDetails} objects for the given customer within the specified date range.
     */
     List<TransactionDetails> findByCustomerAndTransactionDateBetween(CustomerDetails details, LocalDate startDate, LocalDate endDate);

    /**
     * Finds all transaction details associated with a specific customer.
     *
     * @param customer  The {@link CustomerDetails} object representing the customer.
     * @return A list of {@link TransactionDetails} objects for the given customer on or after the start date.
     */
     List<TransactionDetails> findByCustomer(CustomerDetails customer);

    /**
     * Finds all transaction details associated with a specific customer on or after a given start date.
     *
     * @param customer  The {@link CustomerDetails} object representing the customer.
     * @param startDate The starting date (inclusive).
     * @return A list of {@link TransactionDetails} objects for the given customer on or after the start date.
     */
    List<TransactionDetails> findByCustomerAndTransactionDateGreaterThanEqual(CustomerDetails customerDetails, LocalDate startDate);

    /**
     * Finds all transaction details associated with a specific customer on or before a given end date.
     *
     * @param customer The {@link CustomerDetails} object representing the customer.
     * @param endDate  The ending date (inclusive).
     * @return A list of {@link TransactionDetails} objects for the given customer on or before the end date.
     */
    List<TransactionDetails> findByCustomerAndTransactionDateLessThanEqual(CustomerDetails customerDetails, LocalDate endDate);
}

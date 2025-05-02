package com.retailer.rewardcalculator.repository;

import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {/**
 * Retrieves CustomerDetails entity based on its unique identifier.
 *
 * @param customerId The unique identifier of the customer to retrieve.
 * @return the customerDetails entity with the matching {customerId},
 * or null if no such customer exists.
 */
    CustomerDetails findByCustomerId(int customerId);
}

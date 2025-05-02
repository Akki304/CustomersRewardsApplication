package com.retailer.rewardcalculator.controller;

import com.retailer.rewardcalculator.dto.CustomerTransactionDetailsDTO;
import com.retailer.rewardcalculator.exception.CustomerNotFoundException;
import com.retailer.rewardcalculator.model.CustomerDetails;
import com.retailer.rewardcalculator.service.CustomerRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/customers")
public class CustomerRewardContoller
{
    @Autowired
    private CustomerRewardService customerRewardService;

    /**
     *
     * @param customer
     * @return 200 OK if user is created sucessfully
     */
    @PostMapping("/addTransaction")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDetails customer)
    {
        customerRewardService.saveCustomerDetails(customer);
        return new ResponseEntity<>("User successfully created",HttpStatus.OK);
    }

    /**
     *
     * @param customerId
     * @param startDate
     * @param endDate
     * @return retrieve reward data for a specific customer with 200 OK
     * @throws CustomerNotFoundException
     */
    @GetMapping("/{customerId}/rewards")
    public ResponseEntity<?> getRewardPoints(
            @PathVariable int customerId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) throws CustomerNotFoundException {
        try {
            CustomerTransactionDetailsDTO customerTransactionDetailsDTO = customerRewardService.rewardCalculator(customerId, startDate, endDate);
            return new ResponseEntity<>(customerTransactionDetailsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}

package com.retailer.rewardcalculator.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TransactionDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private int transactionAmount;
    @ManyToOne
    @JoinColumn(name = "customerId")
    private CustomerDetails customer;
    private LocalDate transactionDate;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transaction_id) {
        this.transactionId = transaction_id;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public CustomerDetails getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDetails customer) {
        this.customer = customer;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }






}

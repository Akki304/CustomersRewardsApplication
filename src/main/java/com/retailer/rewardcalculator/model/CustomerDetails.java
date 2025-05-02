package com.retailer.rewardcalculator.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer") // mappedBy indicates the owning side
    private List<TransactionDetails> transactions = new ArrayList<>();

    public List<TransactionDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDetails> transactions) {
        this.transactions = transactions;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }


}

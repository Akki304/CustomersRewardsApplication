package com.retailer.rewardcalculator.dto;

import java.util.List;
import java.util.Map;

public class CustomerTransactionDetailsDTO {
    private int customerId;
    private String name;
    private List<TransactionDTO> transaction;
    private Map<String, Integer> monthlyPoints;
    private int totalPoints;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int id) {
        this.customerId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TransactionDTO> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<TransactionDTO> transaction) {
        this.transaction = transaction;
    }

    public Map<String, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}

package com.quandoo.restaurant.model;

public class Customer {

    private int id;
    private String customerFirstName;
    private String customerLastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    @Override
    public String toString() {
        return "Customer [id = " + id + ", customerFirstName = " + customerFirstName + ", customerLastName = " + customerLastName + "]";
    }
}

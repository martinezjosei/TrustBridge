package com.trustbridge.orders.model;

public class NewCustomerOrder {
    
    private String customerName;
    private String dishName;
    private Integer quantity;
    
    public NewCustomerOrder() {
    	
    }
    
    public NewCustomerOrder(String customerName, String dishName, Integer quantity) {
        this.customerName = customerName;
        this.dishName = dishName;
        this.quantity = quantity;
    }

    
    public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

    @Override
    public String toString() {
        return "NewCustomerOrder{" +
               "customerName='" + customerName + '\'' +
               ", dishName='" + dishName + '\'' +
               ", quantity=" + quantity +
               '}';
    }
	
}
package com.trustbridge.orders.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CustomerOrder {
	
    private String dishName;
    private Integer quantity;
    private Boolean isCancelled = false;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
    
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnore  // Prevent infinite loop by ignoring the customer field in JSON serialization
    private Customer customer;

//    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
//    private List<CustomerOrder> orders = new ArrayList<>();
//	
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
	


    public Boolean isCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}


	public String getDishName() {
		return this.dishName;
	}

	public void setDishName(String dishName2) {
		this.dishName = dishName2;
		
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity2) {
		this.quantity = quantity2;		
	}
}
package com.trustbridge.orders.service;

import java.util.Optional;

import com.trustbridge.orders.entities.Customer;

public interface CustomerService {

	Optional<Customer> getCustomerById(Long id);
	
	Customer createCustomer(Customer newCustomer);
}

package com.trustbridge.orders.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trustbridge.orders.entities.Customer;
import com.trustbridge.orders.entities.CustomerOrder;
import com.trustbridge.orders.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
    private final CustomerRepository customerRepository;
    

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getCustomers(int pageNumber, int count) {   
    	 Pageable pagedElems = PageRequest.of(pageNumber, count, Sort.by("id").ascending());
    	 Page<Customer> pageCustomer = customerRepository.findAll(pagedElems);
         List<Customer> customers = pageCustomer.getContent();

         customers.forEach(customer -> {
             System.out.println(customer.getName());
         });
    	 
    	 return pageCustomer;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    
    @Override
    public Customer createCustomer(Customer newCustomer) {
    	Customer createdCustomer = customerRepository.save(newCustomer);  // Save the new customer to the database
    	return createdCustomer;
    }
    
    @Transactional
    public void addOrderToCustomer(Long customerId, CustomerOrder newOrder) {
        // Find the Customer by ID
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Set the Customer reference in the new CustomerOrder
        newOrder.setCustomer(customer);

        // Add the new CustomerOrder to the Customer's order list
        customer.getCustomerOrders().add(newOrder);

        // Save the Customer (CascadeType.ALL will save the new CustomerOrder)
        customerRepository.save(customer);
    }
}


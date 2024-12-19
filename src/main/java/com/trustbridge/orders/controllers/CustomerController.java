package com.trustbridge.orders.controllers;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trustbridge.orders.entities.Customer;
import com.trustbridge.orders.service.CustomerServiceImpl;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
    private final CustomerServiceImpl customerService;

    
    @Autowired
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public Page<Customer> getAllCustomers(
        @RequestParam(defaultValue = "3") String page,
        @RequestParam(defaultValue = "10") String size
    ) {    	
    	return customerService.getCustomers(Integer.parseInt(page) - 1, Integer.parseInt(size));
    }


    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found!");
        }
    }

}

